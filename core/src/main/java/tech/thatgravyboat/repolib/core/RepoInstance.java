package tech.thatgravyboat.repolib.core;

import lombok.AllArgsConstructor;
import tech.thatgravyboat.repolib.core.storage.RepoStorage;


@AllArgsConstructor
public class RepoInstance {
    public final RepoStorage storage;
}
