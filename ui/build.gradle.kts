plugins {
    alias(libs.plugins.kotlinJvmPlugin)
    alias(libs.plugins.kotlinSerializationPlugin)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmToolchain.get()))
    }
}

dependencies {
    implementation(project(":domain"))
}

apply(from = "../dependencies-ui.gradle")
