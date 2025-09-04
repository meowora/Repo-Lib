package tech.thatgravyboat.repolib.core.data.pets;

import com.google.gson.JsonElement;
import tech.thatgravyboat.repolib.core.data.skins.Skin;

import java.util.List;
import java.util.Map;

public record PetTier(
    Skin skin,
    List<JsonElement> lore,
    Map<String, PetVariable> variables
) {
}
