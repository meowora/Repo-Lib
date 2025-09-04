plugins {
    `repo-mod-sourcesets`
    `repo-publishing`
    `backup-repo`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.1.0")
    implementation("com.google.code.gson:gson:2.10")
}