plugins {
    alias(libs.plugins.kotlinAndroidPlugin)
    alias(libs.plugins.androidLibraryPlugin)
    alias(libs.plugins.composeCompilerPlugin)
    alias(libs.plugins.kotlinSerializationPlugin)  // for the NavModel, maybe move this in to domain
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmToolchain.get()))
    }
}

android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerVersion.get()
    }
    namespace = "foo.bar.clean.ui"

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
    implementation(project(":ui"))
    implementation(project(":domain"))
}

apply(from = "../../dependencies-android.gradle")
