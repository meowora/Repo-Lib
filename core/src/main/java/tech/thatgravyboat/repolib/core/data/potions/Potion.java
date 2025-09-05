package tech.thatgravyboat.repolib.core.data.potions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.repolib.core.data.MinecraftLocation;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;

import java.util.Map;

public record Potion(
        RepoLocation repoId,
        MinecraftLocation type,
        Map<String, PotionLevel> levels
) {

    public static final Codec<Potion> CODEC = RecordCodecBuilder.create(it -> it.group(
            RepoCodec.REPO_LOCATION.fieldOf("repo_id").forGetter(Potion::repoId),
            RepoCodec.RESOURCE_LOCATION.fieldOf("type").forGetter(Potion::type),
            RepoCodec.map(Codec.STRING, PotionLevel.CODEC).fieldOf("levels").forGetter(Potion::levels)
    ).apply(it, Potion::new));
}
