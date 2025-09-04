package tech.thatgravyboat.repolib.core.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.*;
import lombok.experimental.WithBy;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.repolib.core.data.skins.Skin;
import tech.thatgravyboat.repolib.core.utils.ExtraRepoCodecs;
import tech.thatgravyboat.repolib.core.utils.RepoUtils;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RequiredArgsConstructor
@With
@WithBy
@Getter
@Setter
public class Item {

    public final MinecraftLocation model;
    public final String skyblockId;
    public final RepoLocation repoId;
    public final JsonElement name;
    public final List<JsonElement> lore;
    @Nullable
    public final Skin skin;
    @Nullable
    public final String variantSelector;
    @Setter(AccessLevel.PRIVATE)
    @Nullable
    public Map<String, Item> variants = null;

    public Codec<Item> CODEC = RecordCodecBuilder.create(it ->
            it.group(
                    MinecraftLocation.CODEC.fieldOf("model").forGetter(Item::getModel),
                    Codec.STRING.fieldOf("skyblock_id").forGetter(Item::getSkyblockId),
                    RepoLocation.CODEC.fieldOf("repo_id").forGetter(Item::getRepoId),
                    ExtraRepoCodecs.JSON.fieldOf("name").forGetter(Item::getName),
                    ExtraRepoCodecs.JSON.listOf().fieldOf("lore").forGetter(Item::getLore),
                    Skin.CODEC.optionalFieldOf("skin").forGetter(RepoUtils.optionalGetter(Item::getSkin)),
                    Codec.STRING.optionalFieldOf("variant_selector").forGetter(RepoUtils.optionalGetter(Item::getVariantSelector))
            ).apply(
                    it,
                    (model, skyblockId, repoId, name, lore, skin, variantSelector) -> new Item(
                            model,
                            skyblockId,
                            repoId,
                            name,
                            lore,
                            skin.orElse(null),
                            variantSelector.orElse(null)
                    )
            )
    );

}
