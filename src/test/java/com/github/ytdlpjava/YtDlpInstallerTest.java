package com.github.ytdlpjava;

import com.github.ytdlpjava.installer.YtDlpInstaller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class YtDlpInstallerTest {

    @Test
    @DisplayName("Should initialize default installer with user home directory")
    void shouldInitDefaultInstaller() {
        YtDlpInstaller installer = YtDlpInstaller.defaultInstaller();
        Path expectedPath = Path.of(System.getProperty("user.home"), ".ytdlp-java", "bin");

        assertThat(installer.getStorageDirectory()).isEqualTo(expectedPath);
    }
}
