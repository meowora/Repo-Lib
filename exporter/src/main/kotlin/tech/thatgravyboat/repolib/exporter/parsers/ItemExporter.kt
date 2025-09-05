package tech.thatgravyboat.repolib.exporter.parsers

import com.google.gson.JsonElement
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.NbtOps
import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.repolib.core.data.RepoLocation
import tech.thatgravyboat.repolib.core.data.items.ExtraData
import tech.thatgravyboat.repolib.core.data.items.Item
import tech.thatgravyboat.repolib.core.data.skins.StaticSkin
import tech.thatgravyboat.repolib.exporter.RepoExporter
import tech.thatgravyboat.repolib.exporter.utils.serialize
import tech.thatgravyboat.repolib.exporter.utils.toMinecraftLocation
import tech.thatgravyboat.skyblockapi.api.datatype.DataTypes
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.MustBeContainer
import tech.thatgravyboat.skyblockapi.api.events.screen.InventoryChangeEvent
import tech.thatgravyboat.skyblockapi.utils.extentions.*

object ItemExporter {
    fun parseItem(itemStack: ItemStack) {
        val data = itemStack[DataComponents.CUSTOM_DATA]?.unsafe ?: return

        val location = RepoExporter.repo.getLocation(NbtOps.INSTANCE, data) ?: return
        if (location.namespace != "item") return
        val variantLess = location.withVariant(null)
        val item = RepoExporter.repo.getOrPut(variantLess) {
            itemStack.createItem(location)
        }

        val finalItem = if (location != variantLess) {
            item.withVariants(mutableMapOf(location.variant() to itemStack.createItem(location)).apply { putAll(item.variants()) })
        } else item

        RepoExporter.repo.put(
            finalItem
                .withLore(finalItem.lore.takeUnless { it.isEmpty() } ?: itemStack.lore())
        )
    }

    fun ItemStack.createItem(location: RepoLocation) = Item(
        BuiltInRegistries.ITEM.getKey(this.getItemModel()).toMinecraftLocation(),
        this[DataTypes.ID],
        location,
        this.hoverName.serialize(),
        this.lore(),
        ExtraData(null),
        this[DataComponents.ENCHANTMENT_GLINT_OVERRIDE] == true,
        emptyMap(),
        this.getTexture()?.let { StaticSkin(it) },
        null
    )

    fun ItemStack.lore(): List<JsonElement> {
        val line = this.getLore().takeUnless { it.isEmpty() } ?: return emptyList()
        if (getRarityLineIndex() == -1) return emptyList()
        return line.subList(0, getRarityLineIndex() + 1).map { it.serialize() }
    }

    @Subscription
    @MustBeContainer
    fun parseContainerItem(event: InventoryChangeEvent) {
        val title = event.title.lowercase()
        if (title.contains("recipe") || title.contains("rewards")) {
            parseItem(event.item)
        }
    }

}