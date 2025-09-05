package tech.thatgravyboat.repolib.core;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.repolib.core.data.RepoLocation;
import tech.thatgravyboat.repolib.core.data.items.Item;
import tech.thatgravyboat.repolib.core.data.pets.Pet;
import tech.thatgravyboat.repolib.core.data.potions.Potion;
import tech.thatgravyboat.repolib.core.data.skins.AnimatedSkinDefinition;
import tech.thatgravyboat.repolib.core.storage.RepoStorage;
import tech.thatgravyboat.repolib.core.utils.RepoType;
import tech.thatgravyboat.repolib.core.utils.RepoUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


@Getter
public class RepoInstance {
    public final RepoStorage storage;

    @Getter(AccessLevel.PRIVATE)
    private final Map<Object, Map<RepoLocation, ?>> mapRegistry = new HashMap<>();

    private final Map<RepoLocation, Item> items = new HashMap<>();
    private final Map<RepoLocation, AnimatedSkinDefinition> animatedSkins = new HashMap<>();
    private final Map<RepoLocation, Pet> pets = new HashMap<>();
    private final Map<RepoLocation, Potion> potions = new HashMap<>();

    public RepoInstance(RepoStorage storage) {
        this.storage = storage;

        registerAndParse(Item.class, items, Item.CODEC, "item");
        registerAndParse(AnimatedSkinDefinition.class, animatedSkins, AnimatedSkinDefinition.CODEC, "skin_definition");
        registerAndParse(Pet.class, pets, Pet.CODEC, "pet");
        registerAndParse(Potion.class, potions, Potion.CODEC, "potion");
    }

    private <T extends RepoType<T>> void registerAndParse(
            Class<T> type,
            Map<RepoLocation, T> storage,
            Codec<T> codec,
            String namespace
    ) {
        register(type, namespace, storage);
        parseAll(this.storage.read(namespace), codec);
    }

    private <T> void register(Class<T> type, String namespace, Map<RepoLocation, T> map) {
        mapRegistry.put(type, map);
        mapRegistry.put(namespace, map);
    }

    public <T extends RepoType<T>> void put(T value) {
        val location = value.repoLocation();
        getMap(value).put(location, value);
        storage.write(location, value.codec().encodeStart(JsonOps.INSTANCE, value).getOrThrow());
    }

    private <T> Map<RepoLocation, T> getMap(T value) {
        return RepoUtils.unsafeCast(mapRegistry.get(value.getClass()));
    }

    private Map<RepoLocation, ?> getMap(String namespace) {
        return RepoUtils.unsafeCast(mapRegistry.get(namespace));
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

    public <T> @Nullable RepoLocation getLocation(DynamicOps<T> ops, T data) {
        return RepoUtils.getRepoLocation(this, ops, data);
    }

    public <T extends RepoType<T>> T getOrPut(RepoLocation location, Supplier<T> provider) {
        Map<RepoLocation, T> map = RepoUtils.unsafeCast(getMap(location.namespace()));

        return map.computeIfAbsent(location, ignored -> provider.get());
    }
}
