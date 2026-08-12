plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.LucasOliveira09"
version = "1.1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = "ytdlp-java"
            version = project.version.toString()

            pom {
                name.set("ytdlp-java")
                description.set("A modern, asynchronous Java 21 library for yt-dlp & FFmpeg.")
                url.set("https://github.com/LucasOliveira09/ytdlp-java")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("LucasOliveira09")
                        name.set("Lucas Oliveira")
                        url.set("https://github.com/LucasOliveira09")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/LucasOliveira09/ytdlp-java.git")
                    developerConnection.set("scm:git:ssh://github.com/LucasOliveira09/ytdlp-java.git")
                    url.set("https://github.com/LucasOliveira09/ytdlp-java")
                }
            }
        }
    }
}
