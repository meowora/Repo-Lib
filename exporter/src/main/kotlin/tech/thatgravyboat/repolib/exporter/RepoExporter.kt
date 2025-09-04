package tech.thatgravyboat.repolib.exporter

import net.fabricmc.api.ClientModInitializer
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.NbtOps
import net.minecraft.world.item.Items
import tech.thatgravyboat.repolib.core.utils.RepoUtils

object RepoExporter : ClientModInitializer {
    override fun onInitializeClient() {
        val item = Items.STRING.defaultInstance

        RepoUtils.getRepoLocation(NbtOps.INSTANCE, item.get(DataComponents.CUSTOM_DATA)!!.unsafe)
    }
}