package com.github.ytdlpjava;

import com.github.ytdlpjava.exception.YtDlpNotFoundException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Main entrance facade for interacting with yt-dlp binary.
 */
public class YtDlpClient {

    private final String ytDlpPath;
    private final String ffmpegPath;
    private final YtDlpProcessExecutor processExecutor;

    public YtDlpClient() {
        this("yt-dlp", null);
    }

    public YtDlpClient(String ytDlpPath) {
        this(ytDlpPath, null);
    }

    public YtDlpClient(String ytDlpPath, String ffmpegPath) {
        this.ytDlpPath = ytDlpPath != null ? ytDlpPath : "yt-dlp";
        this.ffmpegPath = ffmpegPath;
        this.processExecutor = new YtDlpProcessExecutor();
    }

    public static YtDlpClient defaultClient() {
        return new YtDlpClient();
    }

    public static YtDlpClient custom(String ytDlpPath, String ffmpegPath) {
        return new YtDlpClient(ytDlpPath, ffmpegPath);
    }

    /**
     * Executes a download request synchronously.
     *
     * @param request Target request configuration
     * @return Execution response
     */
    public YtDlpResponse execute(YtDlpRequest request) {
        return execute(request, null);
    }

    /**
     * Executes a download request synchronously with a real-time progress callback.
     *
     * @param request  Target request configuration
     * @param callback Callback to receive progress updates
     * @return Execution response
     */
    public YtDlpResponse execute(YtDlpRequest request, YtDlpCallback callback) {
        List<String> command = request.buildCommandList(ytDlpPath, ffmpegPath);
        return processExecutor.execute(command, request.getOutputDir(), callback);
    }

    /**
     * Executes a download request asynchronously using Java 21 CompletableFuture.
     *
     * @param request  Target request configuration
     * @param callback Callback to receive progress updates
     * @return CompletableFuture containing execution response
     */
    public CompletableFuture<YtDlpResponse> executeAsync(YtDlpRequest request, YtDlpCallback callback) {
        return CompletableFuture.supplyAsync(() -> execute(request, callback));
    }

    /**
     * Checks if the yt-dlp executable is available on the system.
     *
     * @return true if yt-dlp is accessible, false otherwise
     */
    public boolean isYtDlpAvailable() {
        try {
            YtDlpResponse response = processExecutor.execute(List.of(ytDlpPath, "--version"), null, null);
            return response.isSuccess();
        } catch (YtDlpNotFoundException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the installed yt-dlp version string.
     *
     * @return Version string (e.g. "2024.03.10")
     */
    public String getVersion() {
        YtDlpResponse response = processExecutor.execute(List.of(ytDlpPath, "--version"), null, null);
        return response.stdOut().trim();
    }

    /**
     * Fetches metadata for a given URL without downloading the media file.
     *
     * @param url Target media URL
     * @return Raw JSON output string containing video metadata
     */
    public String extractMetadataJson(String url) {
        YtDlpRequest request = YtDlpRequest.builder()
                .url(url)
                .addExtraArg("--dump-json")
                .addExtraArg("--no-download")
                .build();
        YtDlpResponse response = execute(request);
        return response.stdOut();
    }

    public String getYtDlpPath() {
        return ytDlpPath;
    }

    public String getFfmpegPath() {
        return ffmpegPath;
    }

    /**
     * Ensures that the yt-dlp binary is available on the system.
     * If yt-dlp is not accessible, automatically downloads the official executable for the current OS.
     *
     * @return YtDlpClient instance pointing to the verified or newly installed binary
     */
    public YtDlpClient ensureInstalled() {
        if (!isYtDlpAvailable()) {
            var installer = com.github.ytdlpjava.installer.YtDlpInstaller.defaultInstaller();
            var installedPath = installer.install();
            return new YtDlpClient(installedPath.toString(), this.ffmpegPath);
        }
        return this;
    }
}
