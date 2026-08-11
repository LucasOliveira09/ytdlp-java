package com.github.ytdlpjava;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class YtDlpResponseTest {

    @Test
    @DisplayName("Should correctly evaluate success response")
    void shouldEvaluateSuccess() {
        YtDlpResponse response = new YtDlpResponse(0, "Done", "", 1200L, List.of(Path.of("video.mp4")));

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.exitCode()).isEqualTo(0);
        assertThat(response.stdOut()).isEqualTo("Done");
        assertThat(response.downloadedFiles()).contains(Path.of("video.mp4"));
    }

    @Test
    @DisplayName("Should evaluate failure response when exit code non-zero")
    void shouldEvaluateFailure() {
        YtDlpResponse response = new YtDlpResponse(1, "", "ERROR: Video unavailable", 500L, List.of());

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.stdErr()).contains("ERROR");
    }
}
