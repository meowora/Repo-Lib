package tech.thatgravyboat.repolib.core.utils;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import org.jetbrains.annotations.ApiStatus;
import tech.thatgravyboat.repolib.core.data.MinecraftLocation;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.data.skins.Skin;

@ApiStatus.Internal
public interface RepoCodec extends Codec<Object> {

    Codec<JsonElement> JSON = Codec.PASSTHROUGH.xmap(
            it -> it.convert(JsonOps.INSTANCE).getValue(),
            it -> new Dynamic<>(JsonOps.INSTANCE, it)
    );

    Codec<RepoLocation> REPO_LOCATION = RepoLocation.CODEC;
    Codec<MinecraftLocation> RESOURCE_LOCATION = MinecraftLocation.CODEC;
    Codec<Skin> SKIN = Skin.CODEC;

    static <K, V> UnboundedMapCodec<K, V> map(final Codec<K> keyCodec, final Codec<V> elementCodec) {
        return new UnboundedMapCodec<>(keyCodec, elementCodec);
    }
}
