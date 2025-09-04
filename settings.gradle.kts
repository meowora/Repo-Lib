rootProject.name = "repo-lib"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.msrandom.net/repository/cloche")
        maven("https://maven.teamresourceful.com/repository/maven-public")
    }
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.google.code.gson:gson:2.13.1")
    }
}

include("exporter")
include("core")
include("lib")