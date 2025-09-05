package tech.thatgravyboat.repolib.core.data.pets;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.repolib.core.data.skins.Skin;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;

import java.util.List;
import java.util.Map;

public record PetTier(
        Skin skin,
        List<JsonElement> lore,
        Map<String, PetVariable> variables
) {

    public static final Codec<PetTier> CODEC = RecordCodecBuilder.create(it -> it.group(
            RepoCodec.SKIN.fieldOf("skin").forGetter(PetTier::skin),
            RepoCodec.JSON.listOf().fieldOf("lore").forGetter(PetTier::lore),
            RepoCodec.map(RepoCodec.STRING, PetVariable.CODEC).fieldOf("variables").forGetter(PetTier::variables)
    ).apply(it, PetTier::new));
}
