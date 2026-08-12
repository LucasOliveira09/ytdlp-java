package com.github.ytdlpjava.exception;

/**
 * Thrown when yt-dlp execution fails with a non-zero exit code or runtime error.
 */
public class YtDlpExecutionException extends YtDlpException {

    private final int exitCode;
    private final String stdOut;
    private final String stdErr;

    public YtDlpExecutionException(String message, int exitCode, String stdOut, String stdErr) {
        super(formatMessage(message, stdErr));
        this.exitCode = exitCode;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    private static String formatMessage(String message, String stdErr) {
        if (stdErr != null && !stdErr.isBlank()) {
            return message + "\nDetails (STDERR):\n" + stdErr.trim();
        }
        return message;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getStdOut() {
        return stdOut;
    }

    public String getStdErr() {
        return stdErr;
    }
}
