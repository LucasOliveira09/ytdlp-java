package com.github.ytdlpjava;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable configuration object defining parameters for a yt-dlp download request.
 * Use {@link Builder} to construct instances.
 */
public final class YtDlpRequest {

    private final String url;
    private final Path outputDir;
    private final String outputFilenameTemplate;
    private final String format;
    private final boolean audioOnly;
    private final String audioFormat;
    private final String audioQuality;
    private final boolean extractThumbnail;
    private final boolean writeSubtitles;
    private final String subtitlesLanguage;
    private final boolean writeJsonMetadata;
    private final boolean noPlaylist;
    private final boolean restrictFilenames;
    private final List<String> extraArgs;

    private YtDlpRequest(Builder builder) {
        this.url = Objects.requireNonNull(builder.url, "URL cannot be null");
        this.outputDir = builder.outputDir;
        this.outputFilenameTemplate = builder.outputFilenameTemplate;
        this.format = builder.format;
        this.audioOnly = builder.audioOnly;
        this.audioFormat = builder.audioFormat;
        this.audioQuality = builder.audioQuality;
        this.extractThumbnail = builder.extractThumbnail;
        this.writeSubtitles = builder.writeSubtitles;
        this.subtitlesLanguage = builder.subtitlesLanguage;
        this.writeJsonMetadata = builder.writeJsonMetadata;
        this.noPlaylist = builder.noPlaylist;
        this.restrictFilenames = builder.restrictFilenames;
        this.extraArgs = Collections.unmodifiableList(new ArrayList<>(builder.extraArgs));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static YtDlpRequest ofUrl(String url) {
        return builder().url(url).build();
    }

    public String getUrl() {
        return url;
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public String getOutputFilenameTemplate() {
        return outputFilenameTemplate;
    }

    public String getFormat() {
        return format;
    }

    public boolean isAudioOnly() {
        return audioOnly;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public String getAudioQuality() {
        return audioQuality;
    }

    public boolean isExtractThumbnail() {
        return extractThumbnail;
    }

    public boolean isWriteSubtitles() {
        return writeSubtitles;
    }

    public String getSubtitlesLanguage() {
        return subtitlesLanguage;
    }

    public boolean isWriteJsonMetadata() {
        return writeJsonMetadata;
    }

    public boolean isNoPlaylist() {
        return noPlaylist;
    }

    public boolean isRestrictFilenames() {
        return restrictFilenames;
    }

    public List<String> getExtraArgs() {
        return extraArgs;
    }

    /**
     * Builds the full CLI command argument list for {@link ProcessBuilder}.
     *
     * @param executablePath Path to yt-dlp binary
     * @param ffmpegLocation Optional path to ffmpeg binary/directory
     * @return List of command strings
     */
    public List<String> buildCommandList(String executablePath, String ffmpegLocation) {
        List<String> cmd = new ArrayList<>();
        cmd.add(executablePath != null ? executablePath : "yt-dlp");

        if (ffmpegLocation != null && !ffmpegLocation.isBlank()) {
            cmd.add("--ffmpeg-location");
            cmd.add(ffmpegLocation);
        }

        if (outputDir != null) {
            String template = outputFilenameTemplate != null ? outputFilenameTemplate : "%(title)s [%(id)s].%(ext)s";
            Path outputPath = outputDir.resolve(template);
            cmd.add("-o");
            cmd.add(outputPath.toString());
        } else if (outputFilenameTemplate != null) {
            cmd.add("-o");
            cmd.add(outputFilenameTemplate);
        }

        if (audioOnly) {
            cmd.add("-x");
            cmd.add("--audio-format");
            cmd.add(audioFormat != null ? audioFormat : "mp3");
            if (audioQuality != null) {
                cmd.add("--audio-quality");
                cmd.add(audioQuality);
            }
        } else if (format != null && !format.isBlank()) {
            cmd.add("-f");
            cmd.add(format);
        }

        if (extractThumbnail) {
            cmd.add("--write-thumbnail");
        }

        if (writeSubtitles) {
            cmd.add("--write-subs");
            if (subtitlesLanguage != null && !subtitlesLanguage.isBlank()) {
                cmd.add("--sub-langs");
                cmd.add(subtitlesLanguage);
            }
        }

        if (writeJsonMetadata) {
            cmd.add("--write-info-json");
        }

        if (noPlaylist) {
            cmd.add("--no-playlist");
        }

        if (restrictFilenames) {
            cmd.add("--restrict-filenames");
        }

        cmd.addAll(extraArgs);
        cmd.add(url);

        return cmd;
    }

    public static final class Builder {
        private String url;
        private Path outputDir;
        private String outputFilenameTemplate;
        private String format;
        private boolean audioOnly = false;
        private String audioFormat = "mp3";
        private String audioQuality = "0";
        private boolean extractThumbnail = false;
        private boolean writeSubtitles = false;
        private String subtitlesLanguage;
        private boolean writeJsonMetadata = false;
        private boolean noPlaylist = false;
        private boolean restrictFilenames = false;
        private final List<String> extraArgs = new ArrayList<>();

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder outputDir(Path outputDir) {
            this.outputDir = outputDir;
            return this;
        }

        public Builder outputFilenameTemplate(String template) {
            this.outputFilenameTemplate = template;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder audioOnly(boolean audioOnly) {
            this.audioOnly = audioOnly;
            return this;
        }

        public Builder audioFormat(String audioFormat) {
            this.audioFormat = audioFormat;
            return this;
        }

        public Builder audioQuality(String audioQuality) {
            this.audioQuality = audioQuality;
            return this;
        }

        public Builder extractThumbnail(boolean extractThumbnail) {
            this.extractThumbnail = extractThumbnail;
            return this;
        }

        public Builder writeSubtitles(boolean writeSubtitles) {
            this.writeSubtitles = writeSubtitles;
            return this;
        }

        public Builder subtitlesLanguage(String subtitlesLanguage) {
            this.subtitlesLanguage = subtitlesLanguage;
            return this;
        }

        public Builder writeJsonMetadata(boolean writeJsonMetadata) {
            this.writeJsonMetadata = writeJsonMetadata;
            return this;
        }

        public Builder noPlaylist(boolean noPlaylist) {
            this.noPlaylist = noPlaylist;
            return this;
        }

        public Builder restrictFilenames(boolean restrictFilenames) {
            this.restrictFilenames = restrictFilenames;
            return this;
        }

        public Builder addExtraArg(String arg) {
            this.extraArgs.add(arg);
            return this;
        }

        public Builder addExtraArgs(List<String> args) {
            this.extraArgs.addAll(args);
            return this;
        }

        public YtDlpRequest build() {
            return new YtDlpRequest(this);
        }
    }
}
