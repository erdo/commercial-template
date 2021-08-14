plugins {
    id("kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.apollographql.apollo3").version("3.8.1") // TODO https://stackoverflow.com/questions/63190420/why-can-t-i-use-val-inside-plugins
    id("app.cash.sqldelight")
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

