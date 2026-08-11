<div align="center">

  <h1>⚡ ytdlp-java</h1>
  <p><b>A next-generation, high-performance Java 21 wrapper for <code>yt-dlp</code> & <code>FFmpeg</code>.</b></p>
  <p><i>Uma biblioteca Java 21 de alta performance e assíncrona para <code>yt-dlp</code> e <code>FFmpeg</code>.</i></p>

  <p>
    <a href="#-english"><img src="https://img.shields.io/badge/Language-English%20%F0%9F%87%BA%F0%9F%87%B8-blue?style=for-the-badge" alt="English"/></a>
    <a href="#-português"><img src="https://img.shields.io/badge/Idioma-Portugu%C3%AAs%20%F0%9F%87%A7%F0%9F%87%B7-green?style=for-the-badge" alt="Português"/></a>
  </p>

  <p>
    <img src="https://img.shields.io/badge/Java-21%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21+"/>
    <img src="https://img.shields.io/badge/Virtual_Threads-Supported-8A2BE2?style=for-the-badge&logo=java" alt="Virtual Threads"/>
    <a href="https://jitpack.io/#LucasOliveira09/ytdlp-java"><img src="https://img.shields.io/badge/JitPack-v1.0.0-success?style=for-the-badge&logo=apache-maven&logoColor=white" alt="JitPack"/></a>
    <img src="https://img.shields.io/badge/License-MIT-007ACC?style=for-the-badge" alt="License MIT"/>
    <img src="https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge&logo=github-actions&logoColor=white" alt="GitHub Actions"/>
  </p>

  ---
</div>

## 📌 Navigation / Navegação

- [🇺🇸 English Documentation](#-english)
  - [Features](#-features)
  - [Architecture](#-architecture-overview)
  - [Installation](#-installation)
  - [Quick Start](#-quick-start)
- [🇧🇷 Documentação em Português](#-português)
  - [Funcionalidades](#-funcionalidades)
  - [Visão Geral da Arquitetura](#-visão-geral-da-arquitetura)
  - [Instalação](#-instalação)
  - [Exemplos Práticos](#-exemplos-práticos)
- [🛣️ Roadmap & Contributing](#%EF%B8%8F-roadmap--contributing)

---

# 🇺🇸 English

### 🌟 Why `ytdlp-java`?

`ytdlp-java` bridges the gap between Java applications and the world's most powerful media downloader tool, `yt-dlp`. Built from scratch leveraging **Java 21 Virtual Threads**, it allows you to download, convert, and inspect videos or audio from over 1,000+ platforms effortlessly.

```
+-------------------------------------------------------------------------+
| [download]  68.4% of ~ 15.42MiB at 4.20MiB/s ETA 00:03                   |
| ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓░░░░░░░░░░░░░░░                              |
+-------------------------------------------------------------------------+
```

### ✨ Features

| Feature | Description |
| :--- | :--- |
| ⚡ **Virtual Threads (Loom)** | Non-blocking process stream ingestion using Java 21 `Executors.newVirtualThreadPerTaskExecutor()`. |
| 🎯 **Fluent Builder API** | Clean, immutable `YtDlpRequest` builder for full control over CLI parameters. |
| 🔄 **Real-Time Progress** | Reactive callback listener delivering percentage, download speed, and ETA updates. |
| 🎵 **Audio Extraction** | Native FFmpeg audio processing (`MP3`, `AAC`, `FLAC`, `WAV`, custom quality presets). |
| 📦 **Zero External Runtime Deps** | Pure Java stdlib implementation over OS `ProcessBuilder`. |
| 🛡️ **Robust Error Handling** | Typed custom exceptions (`YtDlpNotFoundException`, `YtDlpExecutionException`). |

---

### 📐 Architecture Overview

```mermaid
graph TD
    A[Java Application] -->|YtDlpRequest| B[YtDlpClient Facade]
    B -->|Async/Sync| C[YtDlpProcessExecutor]
    C -->|Java 21 Virtual Threads| D[OS ProcessBuilder]
    D -->|Stream STDOUT/STDERR| E[Regex Progress Parser]
    E -->|Real-time Callbacks| F[YtDlpCallback Listener]
    D -->|CLI Execution| G[yt-dlp / FFmpeg]
    G -->|Output Files| H[Local Storage .mp4/.mp3]
```

---

### 📦 Installation

#### Gradle (Kotlin DSL)
```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.LucasOliveira09:ytdlp-java:1.0.0")
}
```

#### Gradle (Groovy DSL)
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.LucasOliveira09:ytdlp-java:1.0.0'
}
```

#### Maven (`pom.xml`)
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

### 💻 Quick Start

#### 1️⃣ Basic Synchronous Download
```java
YtDlpClient client = YtDlpClient.defaultClient();

YtDlpRequest request = YtDlpRequest.builder()
        .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        .outputDir(Path.of("./downloads"))
        .build();

YtDlpResponse response = client.execute(request);
System.out.println("Status: " + response.exitCode() + " | Time: " + response.elapsedTimeMs() + "ms");
```

#### 2️⃣ Asynchronous with Live Progress Updates
```java
YtDlpClient client = YtDlpClient.defaultClient();

YtDlpRequest request = YtDlpRequest.builder()
        .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        .outputDir(Path.of("./downloads"))
        .noPlaylist(true)
        .build();

client.executeAsync(request, (percent, speed, eta, status) -> {
    System.out.printf("Downloading: %.1f%% | Speed: %s | ETA: %s%n", percent, speed, eta);
}).thenAccept(response -> {
    System.out.println("Downloaded files: " + response.downloadedFiles());
}).join();
```

---

# 🇧🇷 Português

### 🌟 Por que usar a `ytdlp-java`?

A `ytdlp-java` conecta o ecossistema Java à ferramenta de download de mídias mais poderosa do mundo, o `yt-dlp`. Construída do zero aproveitando **Virtual Threads do Java 21**, ela permite que você baixe, converta e inspecione áudios e vídeos de mais de 1.000 plataformas com facilidade.

### ✨ Funcionalidades

| Recursos | Descrição |
| :--- | :--- |
| ⚡ **Virtual Threads (Loom)** | Leitura não-bloqueante de processos usando `Executors.newVirtualThreadPerTaskExecutor()`. |
| 🎯 **Fluent Builder API** | Construtor imutável `YtDlpRequest` para controle total dos parâmetros CLI. |
| 🔄 **Progresso em Tempo Real** | Callback reativo com porcentagem, velocidade de download e tempo estimado (ETA). |
| 🎵 **Extração de Áudio** | Processamento nativo via FFmpeg (`MP3`, `AAC`, `FLAC`, `WAV` e qualidades ajustáveis). |
| 📦 **Zero Dependências Externas** | Implementação em Java puro sobre o `ProcessBuilder` nativo do SO. |
| 🛡️ **Exceções Tipadas** | Tratamento robusto de erros com `YtDlpNotFoundException` e `YtDlpExecutionException`. |

---

### 💻 Exemplos Práticos

#### 1️⃣ Baixar Mídia em Áudio (MP3 em Alta Qualidade)
```java
YtDlpRequest request = YtDlpRequest.builder()
        .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        .outputDir(Path.of("./musicas"))
        .audioOnly(true)       // Baixa apenas o áudio
        .audioFormat("mp3")    // Converte para MP3
        .audioQuality("0")     // Qualidade máxima
        .extractThumbnail(true) // Baixa também a capa
        .build();

YtDlpResponse response = YtDlpClient.defaultClient().execute(request);
```

#### 2️⃣ Usando executáveis em caminhos customizados
```java
YtDlpClient client = YtDlpClient.custom(
    "C:\\ferramentas\\yt-dlp.exe", 
    "C:\\ferramentas\\ffmpeg.exe"
);

YtDlpResponse response = client.execute(request);
```

---

## 🛣️ Roadmap & Contributing

- [x] Java 21 Virtual Threads process execution
- [x] Real-time progress parsing regex engine
- [x] Audio extraction & FFmpeg integration
- [x] JitPack publication configuration
- [ ] Built-in automatic `yt-dlp` binary downloader/updater
- [ ] Spring Boot Starter module (`ytdlp-spring-boot-starter`)

Contributions are warmly welcomed! Please feel free to submit a Pull Request or open an Issue.

---

<div align="center">
  <p>Created with ❤️ by <a href="https://github.com/LucasOliveira09">Lucas Oliveira</a></p>
  <p>Distributed under the <b>MIT License</b>.</p>
</div>
