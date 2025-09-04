package tech.thatgravyboat.repolib.core;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;

@UtilityClass
public class RepoLoader {

    public RepoInstance load(Path path, boolean editMode) {
        return new RepoInstance(path, editMode);
    }

}
