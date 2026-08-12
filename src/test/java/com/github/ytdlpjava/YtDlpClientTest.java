package com.github.ytdlpjava;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class YtDlpClientTest {

    @Test
    @DisplayName("Should initialize client with default paths")
    void shouldInitDefaultClient() {
        YtDlpClient client = YtDlpClient.defaultClient();

        assertThat(client.getYtDlpPath()).isEqualTo("yt-dlp");
        assertThat(client.getFfmpegPath()).isNull();
    }

    @Test
    @DisplayName("Should initialize client with custom binary paths")
    void shouldInitCustomClient() {
        YtDlpClient client = YtDlpClient.custom("/usr/bin/yt-dlp", "/usr/bin/ffmpeg");

        assertThat(client.getYtDlpPath()).isEqualTo("/usr/bin/yt-dlp");
        assertThat(client.getFfmpegPath()).isEqualTo("/usr/bin/ffmpeg");
    }

    @Test
    @DisplayName("Should create YtDlpMetadata record correctly")
    void shouldCreateMetadataRecord() {
        YtDlpMetadata metadata = new YtDlpMetadata("Title", "Author", 120, "http://thumb.jpg", "Desc", 1000L);

        assertThat(metadata.title()).isEqualTo("Title");
        assertThat(metadata.uploader()).isEqualTo("Author");
        assertThat(metadata.durationSeconds()).isEqualTo(120);
        assertThat(metadata.thumbnailUrl()).isEqualTo("http://thumb.jpg");
        assertThat(metadata.description()).isEqualTo("Desc");
        assertThat(metadata.viewCount()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("Should return false when yt-dlp binary is not installed/found")
    void shouldReturnFalseWhenYtDlpNotAvailable() {
        YtDlpClient client = YtDlpClient.custom("non_existent_ytdlp_binary_12345", null);

        assertThat(client.isYtDlpAvailable()).isFalse();
    }
}
