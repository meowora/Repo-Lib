package tech.thatgravyboat.repolib.core.utils;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExtraRepoCodecs {
    public Codec<JsonElement> JSON = Codec.PASSTHROUGH.xmap(
            it -> it.convert(JsonOps.INSTANCE).getValue(),
            it -> new Dynamic<>(JsonOps.INSTANCE, it)
    );
}

