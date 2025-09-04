package tech.thatgravyboat.repolib.core;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.repolib.core.data.Item;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.data.pets.Pet;
import tech.thatgravyboat.repolib.core.data.skins.AnimatedSkinDefinition;
import tech.thatgravyboat.repolib.core.storage.RepoStorage;
import tech.thatgravyboat.repolib.core.utils.RepoType;

import java.util.HashMap;
import java.util.Map;


public class RepoInstance {
    public final RepoStorage storage;

    private final Map<Class<?>, Map<RepoLocation, ?>> mapRegistry = new HashMap<>();
    private final Map<RepoLocation, Item> items = new HashMap<>();
    private final Map<RepoLocation, AnimatedSkinDefinition> animatedSkins = new HashMap<>();
    private final Map<RepoLocation, Pet> pets = new HashMap<>();

    public RepoInstance(RepoStorage storage) {
        this.storage = storage;

        register(Item.class, items);
        register(AnimatedSkinDefinition.class, animatedSkins);

        //parseAll(storage.read("pet"), P, Item::repoLocation, items);
        parseAll(storage.read("item"), Item.CODEC);
        parseAll(storage.read("skin_definition"), AnimatedSkinDefinition.CODEC);
    }

    private <T> void register(Class<T> type, Map<RepoLocation, T> map) {
        mapRegistry.put(type, map);
    }

    public <T extends RepoType<T>> void put(T value) {
        val location = value.repoLocation();
        getMap(value).put(location, value);
        storage.write(location, value.codec().encodeStart(JsonOps.INSTANCE, value).getOrThrow());
    }

    private <T> Map<RepoLocation, T> getMap(T value) {
        //noinspection unchecked
        return (Map<RepoLocation, T>) mapRegistry.get(value.getClass());
    }

    private @Nullable <T extends RepoType<T>> T set(T value) {
        return getMap(value).put(value.repoLocation(), value);
    }

    private <T extends RepoType<T>> void parseAll(
            Iterable<JsonElement> elements,
            Codec<T> codec
    ) {
        elements.forEach(it -> {
            val parseResult = codec.parse(JsonOps.INSTANCE, it).getOrThrow();
            @Nullable val oldValue = set(parseResult);
            if (oldValue != null) {
                throw new UnsupportedOperationException("Duplicate repoLocation found!");
            }
        });
    }
}
