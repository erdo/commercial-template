plugins {
    id("kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
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
