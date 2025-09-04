package tech.thatgravyboat.repolib.core.storage;

import com.google.gson.JsonElement;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.utils.RepoUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public record DirectoryStorage(Path root) implements RepoStorage {
    @Override
    public @NotNull Iterable<JsonElement> read(@NotNull String namespace) {
        RepoLocation.ensureValidNamespace(namespace, "");
        val directory = root.resolve(namespace);
        try {
            try (val stream = Files.walk(directory, 1)) {
                val entries = new ArrayList<JsonElement>();
                for (val path : stream.toList()) {
                    entries.add(RepoUtils.parseStrict(
                            JsonElement.class,
                            Files.readString(path, StandardCharsets.UTF_8)));
                }
                return entries;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(@NotNull RepoLocation location, @NotNull JsonElement element) {
        val tempPath = root.resolve(location.namespace()).resolve(location.path());
        final Path path;
        if (location.variant() != null) {
            path = tempPath.resolve(location.variant() + ".json");
        } else {
            path = tempPath.resolveSibling(location.path() + ".json");
        }
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(
                    path,
                    RepoUtils.PRETTY_GSON.toJson(element),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save " + location, e);
        }
    }
}
