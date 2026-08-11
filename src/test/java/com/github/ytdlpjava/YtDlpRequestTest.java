package com.github.ytdlpjava;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class YtDlpRequestTest {

    @Test
    @DisplayName("Should build basic download command with URL")
    void shouldBuildBasicCommand() {
        YtDlpRequest request = YtDlpRequest.ofUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ");

        List<String> cmd = request.buildCommandList("yt-dlp", null);

        assertThat(cmd).containsExactly("yt-dlp", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
    }

    @Test
    @DisplayName("Should build audio extraction command with custom format and quality")
    void shouldBuildAudioExtractionCommand() {
        YtDlpRequest request = YtDlpRequest.builder()
                .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
                .audioOnly(true)
                .audioFormat("mp3")
                .audioQuality("0")
                .noPlaylist(true)
                .outputDir(Path.of("/downloads"))
                .build();

        List<String> cmd = request.buildCommandList("yt-dlp", "/usr/bin/ffmpeg");

        assertThat(cmd).contains(
                "yt-dlp",
                "--ffmpeg-location", "/usr/bin/ffmpeg",
                "-o", Path.of("/downloads").resolve("%(title)s [%(id)s].%(ext)s").toString(),
                "-x",
                "--audio-format", "mp3",
                "--audio-quality", "0",
                "--no-playlist",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException if URL is null")
    void shouldThrowExceptionWhenUrlIsNull() {
        assertThatThrownBy(() -> YtDlpRequest.builder().build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("URL cannot be null");
    }
}
