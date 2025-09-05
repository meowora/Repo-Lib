package tech.thatgravyboat.repolib.core.storage;

import com.google.gson.JsonElement;
import lombok.val;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.utils.RepoUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;

public record DirectoryStorage(Path root) implements RepoStorage {
    @Override
    public Iterable<JsonElement> read(String namespace) {
        RepoLocation.ensureValidNamespace(namespace, "");
        val directory = root.resolve(namespace);
        if (Files.notExists(directory)) {
            return Collections.emptyList();
        }
        try {
            try (val stream = Files.walk(directory, 1)) {
                val entries = new ArrayList<JsonElement>();
                for (val path : stream.toList()) {
                    if (Files.isDirectory(path)) {
                        continue;
                    }
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
    public void write(RepoLocation location, JsonElement element) {
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
