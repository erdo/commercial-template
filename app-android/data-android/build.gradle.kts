plugins {
    alias(libs.plugins.kotlinAndroidPlugin)
    alias(libs.plugins.androidLibraryPlugin)
    alias(libs.plugins.kotlinSerializationPlugin)
    alias(libs.plugins.sqlDelightPlugin)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmToolchain.get()))
    }
}

val appId = "foo.bar.clean.data"

android {
    namespace = appId

    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lint {
        abortOnError = true
        lintConfig = File(project.rootDir, "app/lint-app.xml")
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
}

apply(from = "../../dependencies-android.gradle")

