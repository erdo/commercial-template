plugins {
    alias(libs.plugins.kotlinJvmPlugin)
    alias(libs.plugins.kotlinSerializationPlugin)
    alias(libs.plugins.apolloPlugin)
    alias(libs.plugins.sqlDelightPlugin)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmToolchain.get()))
    }
}

val appId = "foo.bar.clean.data"

apollo {
    service("apollo-fullstack-tutorial") {
        packageName.set(appId)
    }
}

sqldelight {
    databases {
        create("TodoListDatabase") {
            packageName.set("foo.bar.clean.data.db.todoitem")
        }
    }
}

dependencies {
    api(project(":domain"))
}

apply(from = "../dependencies-data.gradle")

