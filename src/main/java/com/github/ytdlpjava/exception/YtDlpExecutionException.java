package com.github.ytdlpjava.exception;

/**
 * Thrown when yt-dlp execution fails with a non-zero exit code or runtime error.
 */
public class YtDlpExecutionException extends YtDlpException {

    private final int exitCode;
    private final String stdOut;
    private final String stdErr;

    public YtDlpExecutionException(String message, int exitCode, String stdOut, String stdErr) {
        super(message);
        this.exitCode = exitCode;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
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
