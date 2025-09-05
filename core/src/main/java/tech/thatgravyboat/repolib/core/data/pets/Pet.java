package tech.thatgravyboat.repolib.core.data.pets;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;

import java.util.Map;

public record Pet(
        RepoLocation repoId,
        JsonElement name,
        Map<String, PetTier> tiers
) {

    public static final Codec<Pet> CODEC = RecordCodecBuilder.create(it -> it.group(
            RepoCodec.REPO_LOCATION.fieldOf("repo_id").forGetter(Pet::repoId),
            RepoCodec.JSON.fieldOf("name").forGetter(Pet::name),
            RepoCodec.map(Codec.STRING, PetTier.CODEC).fieldOf("tiers").forGetter(Pet::tiers)
    ).apply(it, Pet::new));
}
