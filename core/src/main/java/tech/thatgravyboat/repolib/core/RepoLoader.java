package tech.thatgravyboat.repolib.core;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import tech.thatgravyboat.repolib.core.storage.CompactRepoStorage;
import tech.thatgravyboat.repolib.core.storage.DirectoryStorage;
import tech.thatgravyboat.repolib.core.storage.RepoStorage;

import java.nio.file.Path;
import java.util.function.Function;

@UtilityClass
public class RepoLoader {

    public RepoInstance load(Path path, OperationMode mode) {
        return new RepoInstance(mode.repoStorageProvider.apply(path));
    }

    @AllArgsConstructor
    public enum OperationMode {
        COMPACT(CompactRepoStorage::new),
        DEV(DirectoryStorage::new),
        ;

        final Function<Path, RepoStorage> repoStorageProvider;
    }

}
