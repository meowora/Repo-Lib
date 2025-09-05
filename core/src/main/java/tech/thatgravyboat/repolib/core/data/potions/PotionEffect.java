package tech.thatgravyboat.repolib.core.data.potions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;

public record PotionEffect(
        int level,
        String effect,
        int duration
) {

    public static final Codec<PotionEffect> CODEC = RecordCodecBuilder.create(it -> it.group(
            RepoCodec.INT.optionalFieldOf("level", 1).forGetter(PotionEffect::level),
            RepoCodec.STRING.fieldOf("effect").forGetter(PotionEffect::effect),
            RepoCodec.INT.fieldOf("duration").forGetter(PotionEffect::duration
    )).apply(it, PotionEffect::new));
}
