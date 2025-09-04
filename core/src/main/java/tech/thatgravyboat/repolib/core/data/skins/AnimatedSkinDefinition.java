package tech.thatgravyboat.repolib.core.data.skins;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.With;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;
import tech.thatgravyboat.repolib.core.utils.RepoType;

import java.util.List;

@With
public record AnimatedSkinDefinition(
        RepoLocation repoLocation,
        int delay,
        List<String> entries
) implements RepoType<AnimatedSkinDefinition> {

    public static Codec<AnimatedSkinDefinition> CODEC = RecordCodecBuilder.create(it ->
            it.group(
                    RepoCodec.REPO_LOCATION.fieldOf("repo_location").forGetter(AnimatedSkinDefinition::repoLocation),
                    RepoCodec.INT.fieldOf("delay").forGetter(AnimatedSkinDefinition::delay),
                    RepoCodec.STRING.listOf().fieldOf("sequence").forGetter(AnimatedSkinDefinition::entries)
            ).apply(it, AnimatedSkinDefinition::new)
    );

    @Override
    public Codec<AnimatedSkinDefinition> codec() {
        return CODEC;
    }
}
