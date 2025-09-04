package tech.thatgravyboat.repolib.core.data.skins;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;

import java.util.Map;

public interface Skin {

    Map<String, MapCodec<? extends Skin>> REGISTRY = Map.of(
            AnimatedSkin.TYPE, AnimatedSkin.MAP_CODEC,
            StaticSkin.TYPE, StaticSkin.MAP_CODEC
    );
    Codec<Skin> DISATCH_CODEC = RepoCodec.STRING.dispatch(Skin::type, REGISTRY::get);
    Codec<Skin> CODEC = Codec.either(
            RepoCodec.STRING.xmap(StaticSkin::new, StaticSkin::skin),
            DISATCH_CODEC
    ).xmap(
            Either::unwrap,
            skin -> skin instanceof StaticSkin staticSkin ? Either.left(staticSkin) : Either.right(skin)
    );

    String type();

}
