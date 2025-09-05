package tech.thatgravyboat.repolib.exporter

import me.owdding.ktmodules.Module
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.HolderLookup
import net.minecraft.data.registries.VanillaRegistries
import tech.thatgravyboat.repolib.core.RepoInstance
import tech.thatgravyboat.repolib.core.RepoLoader
import tech.thatgravyboat.repolib.exporter.parsers.ItemExporter
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI

@Module
object RepoExporter : ClientModInitializer {
    val registryLookup: HolderLookup.Provider by lazy { VanillaRegistries.createLookup() }

    val repo: RepoInstance = RepoLoader.load(
        FabricLoader.getInstance().configDir.resolve("skyblock_repo"),
        RepoLoader.OperationMode.DEV
    )

    override fun onInitializeClient() {
        listOf(
            RepoExporter,
            ItemExporter
        ).forEach { SkyBlockAPI.eventBus.register(it) }
    }
}