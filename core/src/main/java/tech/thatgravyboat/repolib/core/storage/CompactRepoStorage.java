package tech.thatgravyboat.repolib.core.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.utils.RepoUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public record CompactRepoStorage(Path root) implements RepoStorage {
    @Override
    public @NotNull Iterable<JsonElement> read(@NotNull String namespace) {
        RepoLocation.ensureValidNamespace(namespace, "");
        val file = root.resolve(namespace + ".json");
        try (val stream = new InputStreamReader(Files.newInputStream(file))) {
            return RepoUtils.parseStrict(JsonArray.class, stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load repo data for " + namespace, e);
        }
    }

    @Override
    public void write(@NotNull RepoLocation location, @NotNull JsonElement element) {
        throw new UnsupportedOperationException("Can't save filed in compact mode!");
    }
}
