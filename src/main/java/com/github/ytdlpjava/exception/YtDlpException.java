package com.github.ytdlpjava.exception;

/**
 * Base exception for all errors thrown by ytdlp-java library.
 */
public class YtDlpException extends RuntimeException {

    public YtDlpException(String message) {
        super(message);
    }

    public YtDlpException(String message, Throwable cause) {
        super(message, cause);
    }
}
