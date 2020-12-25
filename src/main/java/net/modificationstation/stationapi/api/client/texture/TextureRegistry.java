package net.modificationstation.stationapi.api.client.texture;

import net.minecraft.client.texture.TextureManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TextureRegistry extends Comparable<TextureRegistry> {

    Map<String, Runnable> RUNNABLES = new HashMap<>();
    Map<String, Supplier<Object>> SUPPLIERS = new HashMap<>();
    Map<String, Function<Object, Object>> FUNCTIONS = new HashMap<>();

    static void unbind() {
        RUNNABLES.get("unbind").run();
    }

    static TextureRegistry getRegistry(String name) {
        return (TextureRegistry) FUNCTIONS.get("getRegistry").apply(name);
    }

    static TextureRegistry getRegistry(Vanilla registry) {
        return getRegistry(registry.name());
    }

    static TextureRegistry currentRegistry() {
        return (TextureRegistry) SUPPLIERS.get("currentRegistry").get();
    }

    @SuppressWarnings("unchecked")
    static Collection<TextureRegistry> registries() {
        return (Collection<TextureRegistry>) SUPPLIERS.get("registries").get();
    }

    String name();

    int ordinal();

    int texturesInLine();

    int texturesInColumn();

    int texturesPerFile();

    String getAtlas(int ID);

    Integer getAtlasID(String path);

    int addAtlas(String atlas);

    void setTexturesInLine(int texturesInLine);

    void setTexturesInColumn(int texturesInColumn);

    int getAtlasTexture(TextureManager textureManager, int ID);

    void bindAtlas(TextureManager textureManager, int ID);

    int currentTexture();

    enum Vanilla {

        TERRAIN,
        PARTICLES,
        GUI_ITEMS,
        GUI_PARTICLES
    }
}
