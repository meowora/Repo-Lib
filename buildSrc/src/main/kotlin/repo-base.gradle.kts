import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.java

plugins {
    java
    idea
}

tasks.register<Jar>("sourcesJar") {
    group = "build"
    archiveClassifier.set("sources")
    sourceSets.map { it.allSource }.forEach {
        from(it)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}