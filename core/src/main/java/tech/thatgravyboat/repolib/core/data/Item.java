package tech.thatgravyboat.repolib.core.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.With;
import lombok.experimental.WithBy;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.repolib.core.data.skins.Skin;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;
import tech.thatgravyboat.repolib.core.utils.RepoType;
import tech.thatgravyboat.repolib.core.utils.RepoUtils;

import java.util.List;
import java.util.Map;

@With
@WithBy
public record Item(
        MinecraftLocation model,
        String skyblockId,
        RepoLocation repoLocation,
        JsonElement name,
        List<JsonElement> lore,
        Map<String, Item> variants,
        @Nullable Skin skin,
        @Nullable String variantSelector
) implements RepoType<Item> {

    public static Codec<Item> CODEC = Codec.recursive("Item", self -> RecordCodecBuilder.create(it ->
            it.group(
                    RepoCodec.RESOURCE_LOCATION.fieldOf("model").forGetter(Item::model),
                    RepoCodec.STRING.fieldOf("skyblock_id").forGetter(Item::skyblockId),
                    RepoCodec.REPO_LOCATION.fieldOf("repo_location").forGetter(Item::repoLocation),
                    RepoCodec.JSON.fieldOf("name").forGetter(Item::name),
                    RepoCodec.JSON.listOf().fieldOf("lore").forGetter(Item::lore),
                    RepoCodec.map(RepoCodec.STRING, self).optionalFieldOf("variants", Map.of()).forGetter(Item::variants),
                    RepoCodec.SKIN.optionalFieldOf("skin").forGetter(RepoUtils.optionalGetter(Item::skin)),
                    RepoCodec.STRING.optionalFieldOf("variant_selector").forGetter(RepoUtils.optionalGetter(Item::variantSelector))
            ).apply(
                    it,
                    (model, skyblockId, repoId, name, lore, variants, skin, selector) -> new Item(
                            model,
                            skyblockId,
                            repoId,
                            name,
                            lore,
                            variants,
                            skin.orElse(null),
                            selector.orElse(null)
                    )
            )
    ));

    @Override
    public Codec<Item> codec() {
        return CODEC;
    }
}