package tech.thatgravyboat.repolib.core.data.skins;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record StaticSkin(String skin) implements Skin {
    public static final String TYPE = "static";
    public static final MapCodec<StaticSkin> MAP_CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    Codec.STRING.fieldOf("skin").forGetter(StaticSkin::skin)
            ).apply(it, StaticSkin::new)
    );
    public static final Codec<StaticSkin> CODEC = MAP_CODEC.codec();

    @Override
    public String type() {
        return TYPE;
    }
}
