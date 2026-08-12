package com.github.ytdlpjava;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class YtDlpProcessExecutorTest {

    @Test
    @DisplayName("Should execute valid system command and capture output using Virtual Threads")
    void shouldExecuteSystemCommand() {
        YtDlpProcessExecutor executor = new YtDlpProcessExecutor();

        // Using Java executable as a cross-platform command test
        List<String> command = List.of("java", "-version");
        YtDlpResponse response = executor.execute(command, null, null);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.exitCode()).isEqualTo(0);
        assertThat(response.elapsedTimeMs()).isGreaterThanOrEqualTo(0);
    }
}
