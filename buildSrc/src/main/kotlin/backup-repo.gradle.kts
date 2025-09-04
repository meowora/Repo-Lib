import com.google.gson.JsonParser
import java.net.URI
import java.nio.file.StandardOpenOption
import kotlin.io.path.*

plugins {
    java
    id("repo-base")
}

val baseUrl = "https://raw.githubusercontent.com/SkyblockAPI/Repo/refs/heads/main/cloudflare"

val downloadRepo: Task = tasks.create("downloadRepo", DefaultTask::class) {
    val outDir = layout.buildDirectory.dir("backup_repo")
    val outDirPath = outDir.get().asFile.toPath().resolve("backup")
    outputs.dir(outDir)
    outputs.upToDateWhen { false }

    fun download(path: String): String = URI.create("$baseUrl/$path").toURL().readText()

    fun getRepoPaths(): List<String> {
        val json = JsonParser.parseString(project.file("repo.json").readText()).asJsonObject
        return listOf(
            json.getAsJsonArray("static").map { it.asString },
            json.getAsJsonArray("versioned").flatMap {
                json.getAsJsonArray("versions").map { version ->
                    "${version.asString}/${it.asString}"
                }
            }
        ).flatten()
    }

    doFirst {
        logger.info("Downloading backup repo!")

        getRepoPaths().forEach { constant ->
            val file = outDirPath.resolve(constant)
            val content = download(constant)
            if (file.parent.notExists()) {
                file.parent.createDirectories()
            }
            file.writeText(
                content,
                Charsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
    }
}

tasks.named("build").configure {
    this.dependsOn(downloadRepo);
    this.mustRunAfter(downloadRepo)
}

sourceSets.main.configure {
    resources.srcDir(downloadRepo.outputs)
}