package com.github.ytdlpjava;

/**
 * Represents metadata information extracted from a media URL without downloading the file.
 *
 * @param title           Title of the video/audio
 * @param uploader        Author/channel name
 * @param durationSeconds Duration in seconds
 * @param thumbnailUrl    Direct URL to the video thumbnail image
 * @param description     Video description text
 * @param viewCount       Total view count
 */
public record YtDlpMetadata(
        String title,
        String uploader,
        int durationSeconds,
        String thumbnailUrl,
        String description,
        long viewCount
) {
}
