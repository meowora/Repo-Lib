package tech.thatgravyboat.repolib.core.data.skins;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.repolib.core.data.RepoLocation;

public record AnimatedSkinDefinition(RepoLocation definition) implements Skin {
    public static final String TYPE = "animated";
    public static final MapCodec<AnimatedSkinDefinition> MAP_CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    RepoLocation.CODEC.fieldOf("definition").forGetter(AnimatedSkinDefinition::definition)
            ).apply(it, AnimatedSkinDefinition::new)
    );

    @Override
    public String type() {
        return TYPE;
    }
}
