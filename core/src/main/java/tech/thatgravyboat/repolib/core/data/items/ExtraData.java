package tech.thatgravyboat.repolib.core.data.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;
import tech.thatgravyboat.repolib.core.utils.RepoUtils;

public record ExtraData(
        @Nullable String modifier
) {

    public static MapCodec<ExtraData> MAP_CODEC = RecordCodecBuilder.mapCodec(it ->
            it.group(
                    RepoCodec.STRING.optionalFieldOf("modifier")
                            .forGetter(RepoUtils.optionalGetter(ExtraData::modifier))
            ).apply(it, (modifier) -> new ExtraData(modifier.orElse(null)))
    );
    public static Codec<ExtraData> CODEC = MAP_CODEC.codec();

}
