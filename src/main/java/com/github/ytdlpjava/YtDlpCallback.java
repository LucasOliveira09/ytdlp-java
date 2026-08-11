package com.github.ytdlpjava;

/**
 * Functional interface for receiving real-time progress updates during download execution.
 */
@FunctionalInterface
public interface YtDlpCallback {

    /**
     * Called periodically when yt-dlp reports progress.
     *
     * @param progressPercentage Current download percentage (0.0 to 100.0)
     * @param speed              Current download speed (e.g., "3.50MiB/s")
     * @param eta                Estimated time remaining (e.g., "00:15")
     * @param status             Current status message or raw line output
     */
    void onProgress(float progressPercentage, String speed, String eta, String status);
}
