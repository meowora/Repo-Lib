package tech.thatgravyboat.repolib.core.utils;

import com.mojang.serialization.Codec;
import tech.thatgravyboat.repolib.core.data.RepoLocation;

public interface RepoType<T> {

    RepoLocation repoLocation();

    Codec<T> codec();

}
