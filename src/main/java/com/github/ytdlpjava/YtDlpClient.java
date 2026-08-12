package com.github.ytdlpjava;

import com.github.ytdlpjava.exception.YtDlpNotFoundException;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Main entrance facade for interacting with yt-dlp binary.
 */
public class YtDlpClient {

    private final String ytDlpPath;
    private final String ffmpegPath;
    private final Path defaultCookiesFile;
    private final String defaultCookiesFromBrowser;
    private final YtDlpProcessExecutor processExecutor;

    public YtDlpClient() {
        this("yt-dlp", null, null, null);
    }

    public YtDlpClient(String ytDlpPath) {
        this(ytDlpPath, null, null, null);
    }

    public YtDlpClient(String ytDlpPath, String ffmpegPath) {
        this(ytDlpPath, ffmpegPath, null, null);
    }

    public YtDlpClient(String ytDlpPath, String ffmpegPath, Path defaultCookiesFile, String defaultCookiesFromBrowser) {
        this.ytDlpPath = ytDlpPath != null ? ytDlpPath : "yt-dlp";
        this.ffmpegPath = ffmpegPath;
        this.defaultCookiesFile = defaultCookiesFile;
        this.defaultCookiesFromBrowser = defaultCookiesFromBrowser;
        this.processExecutor = new YtDlpProcessExecutor();
    }

    public static YtDlpClient defaultClient() {
        return new YtDlpClient();
    }

    public static YtDlpClient custom(String ytDlpPath, String ffmpegPath) {
        return new YtDlpClient(ytDlpPath, ffmpegPath);
    }

    public static Builder builder() {
        return new Builder();
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
        YtDlpRequest effectiveRequest = resolveEffectiveCookies(request);
        List<String> command = effectiveRequest.buildCommandList(ytDlpPath, ffmpegPath);
        return processExecutor.execute(command, effectiveRequest.getOutputDir(), callback);
    }

    private YtDlpRequest resolveEffectiveCookies(YtDlpRequest request) {
        if (request.getCookiesFile() != null || request.getCookiesFromBrowser() != null) {
            return request;
        }

        YtDlpRequest.Builder builder = YtDlpRequest.builder()
                .url(request.getUrl())
                .outputDir(request.getOutputDir())
                .outputFilenameTemplate(request.getOutputFilenameTemplate())
                .format(request.getFormat())
                .audioOnly(request.isAudioOnly())
                .audioFormat(request.getAudioFormat())
                .audioQuality(request.getAudioQuality())
                .extractThumbnail(request.isExtractThumbnail())
                .writeSubtitles(request.isWriteSubtitles())
                .subtitlesLanguage(request.getSubtitlesLanguage())
                .writeJsonMetadata(request.isWriteJsonMetadata())
                .noPlaylist(request.isNoPlaylist())
                .restrictFilenames(request.isRestrictFilenames())
                .addExtraArgs(request.getExtraArgs());

        if (defaultCookiesFile != null && java.nio.file.Files.exists(defaultCookiesFile)) {
            builder.cookiesFile(defaultCookiesFile);
        } else if (defaultCookiesFromBrowser != null && !defaultCookiesFromBrowser.isBlank()) {
            builder.cookiesFromBrowser(defaultCookiesFromBrowser);
        } else if (java.nio.file.Files.exists(java.nio.file.Path.of("cookies.txt"))) {
            builder.cookiesFile(java.nio.file.Path.of("cookies.txt"));
        }

        return builder.build();
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
     * Automatically updates the yt-dlp binary to the latest release version.
     *
     * @return YtDlpResponse containing execution response of yt-dlp -U
     */
    public YtDlpResponse updateYtDlp() {
        return processExecutor.execute(List.of(ytDlpPath, "-U"), null, null);
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

    public Path getDefaultCookiesFile() {
        return defaultCookiesFile;
    }

    public String getDefaultCookiesFromBrowser() {
        return defaultCookiesFromBrowser;
    }

    public static final class Builder {
        private String ytDlpPath = "yt-dlp";
        private String ffmpegPath;
        private Path defaultCookiesFile;
        private String defaultCookiesFromBrowser;

        public Builder ytDlpPath(String path) {
            this.ytDlpPath = path;
            return this;
        }

        public Builder ffmpegPath(String path) {
            this.ffmpegPath = path;
            return this;
        }

        public Builder defaultCookiesFile(Path path) {
            this.defaultCookiesFile = path;
            return this;
        }

        public Builder defaultCookiesFromBrowser(String browser) {
            this.defaultCookiesFromBrowser = browser;
            return this;
        }

        public YtDlpClient build() {
            return new YtDlpClient(ytDlpPath, ffmpegPath, defaultCookiesFile, defaultCookiesFromBrowser);
        }
    }
}
