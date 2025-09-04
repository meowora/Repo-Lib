package tech.thatgravyboat.repolib.core.data.pets;

import com.google.gson.JsonElement;
import tech.thatgravyboat.repolib.core.data.RepoLocation;

import java.util.Map;

public record Pet(
        RepoLocation repoId,
        JsonElement name,
        Map<String, PetTier> tiers
) {
}
