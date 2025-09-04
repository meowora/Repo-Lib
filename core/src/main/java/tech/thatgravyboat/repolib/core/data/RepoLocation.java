package tech.thatgravyboat.repolib.core.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import lombok.With;
import lombok.val;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;
import tech.thatgravyboat.repolib.core.utils.RepoUtils;

public record RepoLocation(
        @Pattern("[a-z0-9_-]+") String namespace,
        @Pattern("[a-z0-9_-]+") String path,
        @Pattern("[a-z0-9_-]+") @With @Nullable String variant
) {
    public static Codec<RepoLocation> CODEC = RepoCodec.STRING.xmap(RepoLocation::parse, RepoLocation::toString);

    @ApiStatus.Internal
    public RepoLocation {
        assert isValidLocation(namespace);
        assert isValidLocation(path);
        assert variant == null || isValidLocation(variant);
    }

    public static RepoLocation parse(String value) {
        return bySeparator(value, ':');
    }

    public static RepoLocation bySeparator(String string, char c) {
        val remainderIndex = string.indexOf(c);
        if (remainderIndex > 0) {
            String remainder = string.substring(remainderIndex + 1);
            val variantIndex = remainder.indexOf(c);
            String namespace = string.substring(0, remainderIndex);
            if (variantIndex == -1) {
                return fromNamespaceAndPath(namespace, remainder);
            } else {
                val path = remainder.substring(0, variantIndex);
                val variant = remainder.substring(variantIndex + 1);
                return fromNamespacePathAndVariant(namespace, path, variant);
            }
        }
        throw new IllegalArgumentException("Can't create repo location without namespace or path");
    }

    public static boolean isValidChar(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9';
    }

    public static boolean isValidLocation(String pathOrVariant) {
        for (int i = 0; i < pathOrVariant.length(); i++) {
            if (!isValidChar(pathOrVariant.charAt(i))) {
                return false;
            }
        }

        return !pathOrVariant.isEmpty();
    }

    public @Pattern("[a-z0-9_-]+") @Subst("item")
    static String ensureValidNamespace(String input, String path) {
        @Subst("item") val lowerInput = RepoUtils.lowercase(input);
        if (!isValidLocation(lowerInput)) {
            throw new IllegalArgumentException("Non [a-z0-9_-] character in namespace of location: " + input + ":" + path);
        }
        return lowerInput;
    }

    public @Pattern("[a-z0-9_-]+") @Subst("item")
    static String ensureValidPath(String namespace, String path) {
        @Subst("item") val lowerInput = RepoUtils.lowercase(path);
        if (!isValidLocation(lowerInput)) {
            throw new IllegalArgumentException("Non [a-z0-9_-] character in path of location: " + namespace + ":" + path);
        }
        return lowerInput;
    }

    public @Pattern("[a-z0-9_-]+") @Subst("item")
    static String ensureValidVariant(String namespace, String path, String variant) {
        @Subst("item") val lowerInput = RepoUtils.lowercase(variant);
        if (!isValidLocation(lowerInput)) {
            throw new IllegalArgumentException("Non [a-z0-9_-] character in variant of location: " + namespace + ":" + path + ":" + variant);
        }
        return lowerInput;
    }

    public static RepoLocation fromNamespaceAndPath(
            @Pattern("[a-z0-9_-]+") String namespace,
            @Pattern("[a-z0-9_-]+") String path
    ) {
        return new RepoLocation(ensureValidNamespace(namespace, path), ensureValidPath(namespace, path), null);
    }

    public static RepoLocation fromNamespacePathAndVariant(
            @Pattern("[a-z0-9_-]+") String namespace,
            @Pattern("[a-z0-9_-]+") String path,
            @Pattern("[a-z0-9_-]+") String variant
    ) {
        return new RepoLocation(
                ensureValidNamespace(namespace, path),
                ensureValidPath(namespace, path),
                ensureValidVariant(namespace, path, variant));
    }

    @Nullable
    public static <T> RepoLocation of(DynamicOps<T> ops, T instance) {
        val optionalMap = ops.getMap(instance).result();

        if (optionalMap.isEmpty()) {
            return null;
        }

        val map = optionalMap.get();
        val optionalIdValue = ops.getStringValue(map.get("id")).result();

        if (optionalIdValue.isEmpty()) {
            return null;
        }

        val idValue = optionalIdValue.get();

        return RepoLocation.fromNamespaceAndPath("a", "b");
    }

    @Override
    public String toString() {
        return namespace + ":" + path + (variant != null ? (":" + variant) : "");
    }
}
