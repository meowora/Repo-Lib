package tech.thatgravyboat.repolib.core.utils;

import com.google.gson.*;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.repolib.core.RepoInstance;
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

    public <T> Optional<String> getString(DynamicOps<T> ops, MapLike<T> map, String field) {
        return Optional.ofNullable(map.get(field)).map(ops::getStringValue).flatMap(DataResult::result);
    }

    public <T> Optional<JsonObject> getJsonObject(DynamicOps<T> ops, MapLike<T> map, String field) {
        return getString(ops, map, field).map(JsonParser::parseString).map(JsonElement::getAsJsonObject);
    }

    public <T> boolean getBoolean(DynamicOps<T> ops, MapLike<T> map, String field) {
        return Optional.ofNullable(map.get(field)).map(ops::getBooleanValue).flatMap(DataResult::result).orElse(false);
    }

    public <T> Optional<String> getField(DynamicOps<T> ops, MapLike<T> map, String field) {
        val number = Optional.ofNullable(map.get(field)).map(ops::getNumberValue).flatMap(DataResult::result);
        val string = getString(ops, map, field);

        return number.map(Number::toString).or(() -> string);
    }

    @Nullable
    public <T> RepoLocation getRepoLocation(RepoInstance repoInstance, DynamicOps<T> ops, T data) {
        val dataMap = ops.getMap(data).result();
        if (dataMap.isEmpty()) {
            return null;
        }
        val map = dataMap.get();

        val optionalId = getString(ops, map, "id");
        if (optionalId.isEmpty()) {
            return null;
        }

        val id = lowercase(optionalId.get()).replace(';', '-').replace(':', '-');
        return switch (id) {
            case "potion" -> {
                val potionTypeOptional = getString(ops, map, "potion_type");
                val potionOptional = getString(ops, map, "potion");
                if (potionTypeOptional.isEmpty() || potionOptional.isEmpty()) {
                    yield potionOptional.map(RepoUtils::potion).orElse(null);
                }

                val isDungeonPotion = getBoolean(ops, map, "dungeon_potion");
                val potionType = lowercase(potionTypeOptional.get());
                val potion = lowercase(potionOptional.get());
                final String path;
                if (!potionType.equals("potion")) {
                    path = potionType;
                } else if (isDungeonPotion) {
                    path = "dungeon";
                } else {
                    path = potion;
                }

                yield potion(path);
            }
            case "pet" -> {
                val petInfoOptional = getJsonObject(ops, map, "petInfo");
                if (petInfoOptional.isEmpty()) {
                    yield null;
                }
                val petInfo = petInfoOptional.get();
                val type = lowercase(petInfo.get("type").getAsString());
                val rarity = lowercase(petInfo.get("tier").getAsString());

                yield RepoLocation.fromNamespacePathAndVariant("pet", type, rarity);
            }
            default -> {
                val itemId = RepoLocation.fromNamespaceAndPath("item", id);
                val item = repoInstance.getItems().get(itemId);
                if (item != null && item.variantSelector() != null) {
                    val variant = getField(ops, map, item.variantSelector());
                    if (variant.isPresent()) {
                        yield itemId.withVariant(variant.get());
                    }
                }

                yield itemId;
            }
        };
    }

    private RepoLocation potion(String path) {
        return RepoLocation.fromNamespaceAndPath("potion", path);
    }

    public <F, T> T unsafeCast(F from) {
        //noinspection unchecked
        return (T) from;
    }

    public <F, T> Function<F, Optional<T>> optionalGetter(Function<F, @Nullable T> mapper) {
        return instance -> Optional.ofNullable(mapper.apply(instance));
    }

}
