package tech.thatgravyboat.repolib.core.data;


import com.mojang.serialization.Codec;

/**
 * Simple copy of the minecraft resource location
 */
public record MinecraftLocation(String namespace, String path) {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    public static Codec<MinecraftLocation> CODEC = Codec.STRING.xmap(MinecraftLocation::parse, MinecraftLocation::toString);

    public MinecraftLocation {
        assert isValidNamespace(namespace);
        assert isValidPath(path);
    }

    public static MinecraftLocation fromNamespaceAndPath(String namespace, String path) {
        assertValid(namespace, path);
        return new MinecraftLocation(namespace, path);
    }

    public static MinecraftLocation parse(String location) {
        return bySeparator(location, ':');
    }

    public static MinecraftLocation bySeparator(String string, char c) {
        int i = string.indexOf(c);
        if (i >= 0) {
            String string2 = string.substring(i + 1);
            if (i != 0) {
                String string3 = string.substring(0, i);
                return fromNamespaceAndPath(string3, string2);
            } else {
                return withDefaultNamespace(string2);
            }
        } else {
            return withDefaultNamespace(string);
        }
    }

    public static MinecraftLocation withDefaultNamespace(String path) {
        return fromNamespaceAndPath(DEFAULT_NAMESPACE, path);
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    public static boolean isAllowedInResourceLocation(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == '_' || c == ':' || c == '/' || c == '.' || c == '-';
    }

    public static boolean validPathChar(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '/' || c == '.';
    }

    private static boolean validNamespaceChar(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '.';
    }

    public static boolean isValidPath(String path) {
        for (int i = 0; i < path.length(); i++) {
            if (!validPathChar(path.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidNamespace(String namespace) {
        for (int i = 0; i < namespace.length(); i++) {
            if (!validNamespaceChar(namespace.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static void assertValidPath(String namespace, String path) {
        if (!isValidPath(path)) {
            throw new IllegalArgumentException("Non [a-z0-9_.-] character in path of location: " + namespace + ":" + path);
        }
    }

    public static void assertValidNamespace(String namespace, String path) {
        if (!isValidNamespace(path)) {
            throw new IllegalArgumentException("Non [a-z0-9/._-] character in path of location: " + namespace + ":" + path);
        }
    }

    public static void assertValid(String namespace, String path) {
        assertValidNamespace(namespace, path);
        assertValidPath(namespace, path);
    }
}
