plugins {
    `maven-publish`
    id("repo-base")
}

abstract class RepoPublishingData {
    abstract val name: Property<String>
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "repo-${project.name}"
            from(components["java"])

            pom {
                name.set("Repo-${project.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}")
                url.set("https://github.com/SkyblockAPI/Repo-Lib")

                scm {
                    connection.set("git:https://github.com/SkyblockAPI/Repo-Lib.git")
                    developerConnection.set("git:https://github.com/SkyblockAPI/Repo-Lib.git")
                    url.set("https://github.com/SkyblockAPI/Repo-Lib")
                }
            }
        }
    }
    repositories {
        maven {
            setUrl("https://maven.teamresourceful.com/repository/thatgravyboat/")
            credentials {
                username = System.getenv("MAVEN_USER") ?: providers.gradleProperty("maven_username").orNull
                password = System.getenv("MAVEN_PASS") ?: providers.gradleProperty("maven_password").orNull
            }
        }
    }
}