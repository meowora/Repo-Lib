plugins {
    `repo-publishing`
}

repositories {
    maven("https://libraries.minecraft.net")
    mavenCentral()
}

dependencies {
    implementation("com.mojang:datafixerupper:8.0.16")
    compileOnly("org.jetbrains:annotations:26.0.2")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}