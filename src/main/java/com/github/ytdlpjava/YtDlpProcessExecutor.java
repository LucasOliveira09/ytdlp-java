package com.github.ytdlpjava;

import com.github.ytdlpjava.exception.YtDlpExecutionException;
import com.github.ytdlpjava.exception.YtDlpNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Low-level execution engine that spawns OS processes for yt-dlp using Java 21 Virtual Threads.
 */
public class YtDlpProcessExecutor {

    private static final Pattern PROGRESS_PATTERN = Pattern.compile(
            "\\[download\\]\\s+(\\d+(?:\\.\\d+)?)%\\s+of\\s+~?\\s*\\S+\\s+at\\s+(\\S+)\\s+ETA\\s+(\\S+)"
    );

    private static final Pattern MERGER_DESTINATION_PATTERN = Pattern.compile(
            "\\[Merger\\]\\s+Merging\\s+formats\\s+into\\s+\"([^\"]+)\""
    );

    private static final Pattern DOWNLOAD_DESTINATION_PATTERN = Pattern.compile(
            "\\[download\\]\\s+Destination:\\s+(.+)"
    );

    private static final Pattern ALREADY_DOWNLOADED_PATTERN = Pattern.compile(
            "\\[download\\]\\s+(.+)\\s+has\\s+already\\s+been\\s+downloaded"
    );

    public YtDlpResponse execute(List<String> command, Path workingDirectory, YtDlpCallback callback) {
        long startTime = System.currentTimeMillis();

        ProcessBuilder pb = new ProcessBuilder(command);
        if (workingDirectory != null) {
            try {
                Files.createDirectories(workingDirectory);
            } catch (IOException ignored) {
            }
            pb.directory(workingDirectory.toFile());
        }

        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            String executable = command.isEmpty() ? "yt-dlp" : command.get(0);
            throw new YtDlpNotFoundException("Failed to start process for command: " + executable +
                    ". Ensure yt-dlp is installed and available in PATH.", e);
        }

        StringBuilder stdOutBuilder = new StringBuilder();
        StringBuilder stdErrBuilder = new StringBuilder();
        List<Path> downloadedFiles = Collections.synchronizedList(new ArrayList<>());

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Task 1: Read stdout asynchronously using Virtual Threads
            executor.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stdOutBuilder.append(line).append(System.lineSeparator());
                        parseProgressLine(line, callback);
                        parseDownloadedFilePath(line, downloadedFiles);
                    }
                } catch (IOException ignored) {
                }
            });

            // Task 2: Read stderr asynchronously using Virtual Threads
            executor.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stdErrBuilder.append(line).append(System.lineSeparator());
                    }
                } catch (IOException ignored) {
                }
            });
        } // Auto-closes and waits for virtual thread tasks to finish

        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
            throw new YtDlpExecutionException("Execution interrupted", -1, stdOutBuilder.toString(), stdErrBuilder.toString());
        }

        long elapsedTimeMs = System.currentTimeMillis() - startTime;
        YtDlpResponse response = new YtDlpResponse(
                exitCode,
                stdOutBuilder.toString(),
                stdErrBuilder.toString(),
                elapsedTimeMs,
                List.copyOf(downloadedFiles)
        );

        if (exitCode != 0) {
            throw new YtDlpExecutionException(
                    "yt-dlp process exited with status code " + exitCode,
                    exitCode,
                    response.stdOut(),
                    response.stdErr()
            );
        }

        return response;
    }

    private void parseProgressLine(String line, YtDlpCallback callback) {
        if (callback == null) return;

        Matcher matcher = PROGRESS_PATTERN.matcher(line);
        if (matcher.find()) {
            try {
                float percentage = Float.parseFloat(matcher.group(1));
                String speed = matcher.group(2);
                String eta = matcher.group(3);
                callback.onProgress(percentage, speed, eta, line.trim());
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private void parseDownloadedFilePath(String line, List<Path> downloadedFiles) {
        Matcher downloadMatcher = DOWNLOAD_DESTINATION_PATTERN.matcher(line);
        if (downloadMatcher.find()) {
            addPathIfExists(downloadMatcher.group(1).trim(), downloadedFiles);
            return;
        }

        Matcher mergerMatcher = MERGER_DESTINATION_PATTERN.matcher(line);
        if (mergerMatcher.find()) {
            addPathIfExists(mergerMatcher.group(1).trim(), downloadedFiles);
            return;
        }

        Matcher alreadyMatcher = ALREADY_DOWNLOADED_PATTERN.matcher(line);
        if (alreadyMatcher.find()) {
            addPathIfExists(alreadyMatcher.group(1).trim(), downloadedFiles);
        }
    }

    private void addPathIfExists(String pathStr, List<Path> downloadedFiles) {
        try {
            Path path = Paths.get(pathStr);
            downloadedFiles.add(path);
        } catch (Exception ignored) {
        }
    }
}
