package com.github.ytdlpjava;

import java.nio.file.Path;
import java.util.List;

/**
 * Java 21 Record representing the result of a yt-dlp command execution.
 *
 * @param exitCode        Exit code of the process (0 indicates success)
 * @param stdOut          Standard output content
 * @param stdErr          Standard error content
 * @param elapsedTimeMs   Total duration of execution in milliseconds
 * @param downloadedFiles List of resolved file paths created by the execution
 */
public record YtDlpResponse(
        int exitCode,
        String stdOut,
        String stdErr,
        long elapsedTimeMs,
        List<Path> downloadedFiles
) {
    public boolean isSuccess() {
        return exitCode == 0;
    }
}
