package tech.thatgravyboat.repolib.core.utils;

import com.mojang.serialization.DynamicOps;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.repolib.core.data.RepoLocation;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@UtilityClass
public class RepoUtils {
    public String lowercase(String input) {
        return input.toLowerCase(Locale.ROOT);
    }

    @Nullable
    public <T> RepoLocation getRepoLocation(DynamicOps<T> ops, T data) {
        val id = ops.getMap(data);

        return null;
    }

    public <F, T> Function<F, Optional<T>> optionalGetter(Function<F, T> mapper) {
        return instance -> Optional.ofNullable(mapper.apply(instance));
    }

}
