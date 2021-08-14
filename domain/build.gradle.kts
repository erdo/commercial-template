plugins {
    id("kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmToolchain.get()))
    }
}

apply(from = "../dependencies-domain.gradle")
