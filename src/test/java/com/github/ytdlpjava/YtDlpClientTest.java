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
}
