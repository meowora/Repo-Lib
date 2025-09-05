package tech.thatgravyboat.repolib.exporter.utils

import com.google.gson.JsonElement
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import tech.thatgravyboat.repolib.core.data.MinecraftLocation
import tech.thatgravyboat.repolib.exporter.RepoExporter.registryLookup
import tech.thatgravyboat.skyblockapi.utils.json.Json.toJsonOrThrow
import kotlin.jvm.optionals.getOrNull


fun <T> ResourceKey<T>.get(): Holder<T>? = registryLookup.get(this).getOrNull()
fun <T> ResourceKey<Registry<T>>.lookup(): HolderLookup.RegistryLookup<T> = registryLookup.lookupOrThrow(this)
fun <T> ResourceKey<Registry<T>>.get(value: T): Holder<T> =
    this.lookup().filterElements { it == value }.listElements().findFirst().orElseThrow()

fun <T> ResourceKey<Registry<T>>.get(value: ResourceLocation): Holder<T> = runCatching {
    this.lookup().listElements().filter {
        it.unwrapKey().get().location() == value
    }.findFirst().orElseThrow()
}.onFailure {
    throw RuntimeException("Failed to load $value from registry ${this.location()}", it)
}.getOrThrow()

fun Component.serialize(): JsonElement {
    return this.toJsonOrThrow(ComponentSerialization.CODEC)
}

fun ResourceLocation.toMinecraftLocation(): MinecraftLocation =
    MinecraftLocation.fromNamespaceAndPath(this.namespace, this.path)

fun MinecraftLocation.toResourceLocation(): ResourceLocation =
    ResourceLocation.fromNamespaceAndPath(this.namespace, this.path)