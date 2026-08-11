package com.github.ytdlpjava.exception;

/**
 * Thrown when the yt-dlp binary is not found on PATH or specified location.
 */
public class YtDlpNotFoundException extends YtDlpException {

    public YtDlpNotFoundException(String message) {
        super(message);
    }

    public YtDlpNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
