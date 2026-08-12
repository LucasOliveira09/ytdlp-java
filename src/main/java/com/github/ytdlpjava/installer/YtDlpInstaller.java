package com.github.ytdlpjava.installer;

import com.github.ytdlpjava.exception.YtDlpException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility for automatically downloading and installing official yt-dlp binaries
 * matching the host operating system (Windows, Linux, macOS).
 */
public class YtDlpInstaller {

    private static final String BASE_RELEASE_URL = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/";
    private final Path storageDirectory;

    public YtDlpInstaller() {
        this(Paths.get(System.getProperty("user.home"), ".ytdlp-java", "bin"));
    }

    public YtDlpInstaller(Path storageDirectory) {
        this.storageDirectory = storageDirectory;
    }

    public static YtDlpInstaller defaultInstaller() {
        return new YtDlpInstaller();
    }

    /**
     * Downloads and installs the latest official yt-dlp executable for the current OS.
     *
     * @return Path to the installed executable binary
     */
    public Path install() {
        String binaryName = getBinaryNameForOs();
        String downloadUrl = BASE_RELEASE_URL + binaryName;
        Path targetPath = storageDirectory.resolve(binaryName);

        try {
            Files.createDirectories(storageDirectory);
            if (Files.exists(targetPath) && Files.size(targetPath) > 0) {
                setExecutablePermissions(targetPath);
                return targetPath;
            }

            downloadFile(downloadUrl, targetPath);
            setExecutablePermissions(targetPath);
            return targetPath;
        } catch (IOException e) {
            throw new YtDlpException("Failed to automatically install yt-dlp binary from " + downloadUrl, e);
        }
    }

    private void downloadFile(String urlString, Path destination) throws IOException {
        URL url = URI.create(urlString).toURL();
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream fileOut = new FileOutputStream(destination.toFile())) {
            byte[] dataBuffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 8192)) != -1) {
                fileOut.write(dataBuffer, 0, bytesRead);
            }
        }
    }

    private void setExecutablePermissions(Path path) {
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win")) {
            path.toFile().setExecutable(true, false);
        }
    }

    private String getBinaryNameForOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "yt-dlp.exe";
        } else if (os.contains("mac")) {
            return "yt-dlp_macos";
        } else {
            return "yt-dlp";
        }
    }

    public Path getStorageDirectory() {
        return storageDirectory;
    }
}
