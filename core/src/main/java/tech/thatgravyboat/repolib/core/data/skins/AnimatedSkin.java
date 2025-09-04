package tech.thatgravyboat.repolib.core.data.skins;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;

public record AnimatedSkin(RepoLocation definition) implements Skin {
    public static final String TYPE = "animated";

    public static final MapCodec<AnimatedSkin> MAP_CODEC = RepoCodec.REPO_LOCATION
            .fieldOf("definition")
            .xmap(AnimatedSkin::new, AnimatedSkin::definition);
    public static final Codec<AnimatedSkin> CODEC = MAP_CODEC.codec();

    @Override
    public String type() {
        return TYPE;
    }
}
