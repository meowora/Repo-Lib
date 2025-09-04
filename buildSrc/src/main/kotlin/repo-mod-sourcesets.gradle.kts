
plugins {
    java
    id("repo-base")
}

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.neoforged.net/releases")
}

val fabric: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

val neoforge: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

dependencies {
    "fabricImplementation"("net.fabricmc:fabric-loader:0.15.0") { isTransitive = false}
    "neoforgeImplementation"("net.neoforged.fancymodloader:loader:3.0.13") { isTransitive = false}
}

tasks.jar {
    from(fabric.output)
    from(neoforge.output)
}