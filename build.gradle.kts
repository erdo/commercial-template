/**
 * ./gradlew check
 * ./gradlew test
 * ./gradlew testDebugUnitTest
 * ./gradlew connectedAndroidTest -PtestBuildType=debug --no-daemon --no-parallel --continue
 * ./gradlew connectedAndroidTest -PtestBuildType=release
 * ./gradlew app:testDebugUnitTest --info
 * ./gradlew app:dependencies --configuration releaseRuntimeClasspath
 */
plugins {
    alias(libs.plugins.androidAppPlugin).apply(false)
    alias(libs.plugins.androidLibraryPlugin).apply(false)
    alias(libs.plugins.kotlinJvmPlugin).apply(false)
    alias(libs.plugins.kotlinAndroidPlugin).apply(false)
    alias(libs.plugins.composeCompilerPlugin).apply(false)
    alias(libs.plugins.kotlinSerializationPlugin).apply(false)
    alias(libs.plugins.kotlinKaptPlugin).apply(false)
}
