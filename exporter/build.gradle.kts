plugins {
    `repo-base`
    kotlin("jvm") version "2.2.0"
    id("earth.terrarium.cloche") version "0.13.4"
    id("me.owdding.gradle") version "1.0.5"
}

repositories {
    cloche {
        librariesMinecraft()
        mavenCentral()
        maven("https://repo.hypixel.net/repository/Hypixel/")
        maven("https://api.modrinth.com/maven")
        maven("https://maven.teamresourceful.com/repository/maven-public")

        mavenParchment()
        mavenFabric()

        main()
    }
}

dependencies {
    implementation(project(":core"))
}

cloche {
    metadata {
        modId = "repo-exporter"
        license = ""
    }

    minecraftVersion = "1.21.8"
    singleTarget {
        fabric {
            dependencies {
                implementation("tech.thatgravyboat:skyblock-api:2.3.4") {
                    exclude("me.djtheredstoner")
                }
            }
            metadata {
                entrypoint("client") {
                    adapter = "kotlin"
                    value = "tech.thatgravyboat.repolib.exporter.RepoExporter"
                }
            }

            loaderVersion = "0.17.0"
            includedClient()

            runs { client() }
            meowdding.handleTarget(this)
        }
    }

    mappings {
        official()
        parchment("2025.07.20")
    }
}