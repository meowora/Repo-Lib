package tech.thatgravyboat.repolib.exporter

import net.fabricmc.api.ClientModInitializer
import net.minecraft.world.item.Items

object RepoExporter : ClientModInitializer {
    override fun onInitializeClient() {
        val item = Items.STRING.defaultInstance
    }
}