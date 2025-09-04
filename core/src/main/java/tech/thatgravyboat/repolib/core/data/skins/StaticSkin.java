package tech.thatgravyboat.repolib.core.data.skins;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;

public record StaticSkin(String skin) implements Skin {
    public static final String TYPE = "static";

    public static final MapCodec<StaticSkin> MAP_CODEC = RepoCodec.STRING
            .fieldOf("skin")
            .xmap(StaticSkin::new, StaticSkin::skin);
    public static final Codec<StaticSkin> CODEC = MAP_CODEC.codec();

    @Override
    public String type() {
        return TYPE;
    }
}
