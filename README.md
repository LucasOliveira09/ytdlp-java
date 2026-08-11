# 🚀 ytdlp-java

<p align="center">
  <img src="https://img.shields.io/badge/Java-21%2B-orange?style=for-the-badge&logo=openjdk" alt="Java 21+"/>
  <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="License MIT"/>
  <a href="https://jitpack.io/#LucasOliveira09/ytdlp-java"><img src="https://img.shields.io/badge/JitPack-v1.0.0-green?style=for-the-badge&logo=apache-maven" alt="JitPack"/></a>
  <img src="https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge&logo=github-actions" alt="GitHub Actions"/>
</p>

A modern, high-performance, asynchronous **Java 21** library and wrapper for [yt-dlp](https://github.com/yt-dlp/yt-dlp) and **FFmpeg**. Designed with **Virtual Threads**, a fluent **Builder API**, and **real-time progress callbacks**.

---

## ✨ Features

- ⚡ **Java 21 Virtual Threads**: Non-blocking stream handling for high concurrency.
- 🎯 **Fluent Builder API**: Easily configure download formats, paths, audio conversion, and metadata.
- 🔄 **Real-Time Progress Callbacks**: Receive percentage, speed, and ETA updates during download.
- 🚀 **Synchronous & Asynchronous**: Support for blocking calls or `CompletableFuture`.
- 🎵 **Audio Extraction**: Native options for MP3, AAC, FLAC, WAV, and quality selection via FFmpeg.
- 🛠️ **Zero External Runtime Dependencies**: Pure Java implementation around `ProcessBuilder`.

---

## 📦 Installation

This library is published via **JitPack**. Add the repository and dependency to your project:

### 🐘 Gradle (Kotlin DSL)
```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.LucasOliveira09:ytdlp-java:1.0.0")
}
```

### 🐘 Gradle (Groovy DSL)
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.LucasOliveira09:ytdlp-java:1.0.0'
}
```

### 📦 Maven (`pom.xml`)
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.LucasOliveira09</groupId>
    <artifactId>ytdlp-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 💻 Quick Start Examples

### 1️⃣ Basic Video Download (Synchronous)

```java
import com.github.ytdlpjava.YtDlpClient;
import com.github.ytdlpjava.YtDlpRequest;
import com.github.ytdlpjava.YtDlpResponse;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        YtDlpClient client = YtDlpClient.defaultClient();

        YtDlpRequest request = YtDlpRequest.builder()
                .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
                .outputDir(Path.of("./downloads"))
                .build();

        YtDlpResponse response = client.execute(request);

        if (response.isSuccess()) {
            System.out.println("Download completed in " + response.elapsedTimeMs() + "ms!");
        }
    }
}
```

---

### 2️⃣ Asynchronous Download with Real-Time Progress Callback

```java
import com.github.ytdlpjava.YtDlpClient;
import com.github.ytdlpjava.YtDlpRequest;
import java.nio.file.Path;

public class AsyncExample {
    public static void main(String[] args) {
        YtDlpClient client = YtDlpClient.defaultClient();

        YtDlpRequest request = YtDlpRequest.builder()
                .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
                .outputDir(Path.of("./downloads"))
                .noPlaylist(true)
                .build();

        client.executeAsync(request, (percent, speed, eta, status) -> {
            System.out.printf("Progress: %.1f%% | Speed: %s | ETA: %s%n", percent, speed, eta);
        }).thenAccept(response -> {
            System.out.println("Finished! Output files: " + response.downloadedFiles());
        }).join();
    }
}
```

---

### 3️⃣ Convert Video to Audio (MP3)

```java
YtDlpRequest request = YtDlpRequest.builder()
        .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        .outputDir(Path.of("./music"))
        .audioOnly(true)
        .audioFormat("mp3")
        .audioQuality("0") // Best quality
        .build();

YtDlpResponse response = YtDlpClient.defaultClient().execute(request);
```

---

## 🛠️ System Prerequisites

Make sure `yt-dlp` (and optionally `ffmpeg` for audio conversion) is installed on your system or accessible via PATH.

- **Linux / macOS**: `sudo curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/local/bin/yt-dlp && sudo chmod a+rx /usr/local/bin/yt-dlp`
- **Windows (winget)**: `winget install yt-dlp` or `winget install Gyan.FFmpeg`

> 💡 **Custom Path**: You can also specify custom binary locations directly:
> ```java
> YtDlpClient client = YtDlpClient.custom("C:\\tools\\yt-dlp.exe", "C:\\tools\\ffmpeg.exe");
> ```

---

## 🤝 Contributing

Contributions are welcome! Feel free to open an **issue** or submit a **Pull Request**.

1. Fork the project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the **MIT License**. See `LICENSE` for more information.
