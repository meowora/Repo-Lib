package tech.thatgravyboat.repolib.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.DynamicOps;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.repolib.core.data.RepoLocation;

import java.io.IOException;
import java.io.Reader;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@UtilityClass
public class RepoUtils {
    public Gson GSON = new GsonBuilder().create();
    public Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public <T> T parseStrict(Class<T> type, Reader reader) throws IOException {
        return GSON.getAdapter(type).fromJson(reader);
    }

    public <T> T parseStrict(Class<T> type, String json) throws IOException {
        return GSON.getAdapter(type).fromJson(json);
    }

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
