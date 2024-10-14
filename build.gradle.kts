plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "solutions.sulfura"
version = "1.0-RELEASE"

apply(from = "config/settings.env.gradle.kts")

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.2.6")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("com.intellij.java", "org.intellij.intelliLang"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("")
    }

    signPlugin {
        certificateChain.set(System.getenv(file("config/chain.crt").readText()))
        privateKey.set(System.getenv(file("config/private.pem").readText()))
        password.set(project.properties.getOrDefault("PRIVATE_KEY_PASSWORD", "").toString())
    }

    publishPlugin {
        token.set(project.properties.getOrDefault("PUBLISH_TOKEN", "").toString())
    }

}
