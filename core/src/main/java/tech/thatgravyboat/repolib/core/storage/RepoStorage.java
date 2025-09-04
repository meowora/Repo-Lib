package tech.thatgravyboat.repolib.core.storage;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.repolib.core.data.RepoLocation;

public interface RepoStorage {

    @NotNull Iterable<JsonElement> read(@NotNull String namespace);

    void write(@NotNull RepoLocation location, @NotNull JsonElement element);

}
