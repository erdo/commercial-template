/**
 * ./gradlew check
 * ./gradlew test
 * ./gradlew testDebugUnitTest
 * ./gradlew connectedAndroidTest -PtestBuildType=debug --no-daemon --no-parallel --continue
 * ./gradlew connectedAndroidTest -PtestBuildType=release
 * ./gradlew app:testDebugUnitTest --info
 * ./gradlew app:dependencies --configuration releaseRuntimeClasspath
 */
buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.kotlinGradlePlugin)
        classpath(libs.kotlin.serialization)
        classpath(libs.androidGradlePlugin)
        classpath(libs.sqldelightGradlePlugin)
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}
