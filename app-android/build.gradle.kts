plugins {
    alias(libs.plugins.kotlinAndroidPlugin)
    alias(libs.plugins.androidAppPlugin)
    alias(libs.plugins.kotlinSerializationPlugin)
    alias(libs.plugins.kotlinKaptPlugin)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmToolchain.get()))
    }
}

android {
    namespace = "foo.bar.clean"

    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        versionCode = 1
        versionName = "0.5"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lint {
        abortOnError = true
        lintConfig = File(project.rootDir, "app/lint-app.xml")
    }

    buildFeatures {
        buildConfig = true
    }

    signingConfigs {
        create("release") {
            // keytool -genkey -v -keystore debug.fake_keystore -storetype PKCS12 -alias android -storepass android -keypass android -keyalg RSA -keysize 2048 -validity 20000 -dname "cn=Unknown, ou=Unknown, o=Unknown, c=Unknown"
            storeFile = project.file("../debug.fake_keystore")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-app.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {

    implementation(project(":app-android:data-android"))
    implementation(project(":app-android:ui-android"))

    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":ui"))

    // only test dependencies listed here

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.11.0")

    androidTestImplementation("io.mockk:mockk-android:1.11.0")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.annotation:annotation:1.6.0")
}

apply(from = "../dependencies-android.gradle")
apply(from = "../dependencies-data.gradle")
apply(from = "../dependencies-domain.gradle")
