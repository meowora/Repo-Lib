package tech.thatgravyboat.repolib.core.data.potions;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;

import java.util.List;

public record PotionLevel(
        List<JsonElement> lore,
        List<PotionEffect> effects
) {

    public static final Codec<PotionLevel> CODEC = RecordCodecBuilder.create(it -> it.group(
            RepoCodec.JSON.listOf().fieldOf("lore").forGetter(PotionLevel::lore),
            PotionEffect.CODEC.listOf().fieldOf("effects").forGetter(PotionLevel::effects)
    ).apply(it, PotionLevel::new));
}
