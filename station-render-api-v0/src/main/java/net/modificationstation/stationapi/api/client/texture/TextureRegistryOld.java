package net.modificationstation.stationapi.api.client.texture;

import net.minecraft.client.texture.TextureManager;

import java.util.*;
import java.util.function.*;

@Deprecated
public interface TextureRegistryOld extends Comparable<TextureRegistryOld> {

    Map<String, Runnable> RUNNABLES = new HashMap<>();
    Map<String, Supplier<Object>> SUPPLIERS = new HashMap<>();
    Map<String, Function<Object, Object>> FUNCTIONS = new HashMap<>();

    static void unbind() {
        RUNNABLES.get("unbind").run();
    }

    static TextureRegistryOld getRegistry(String name) {
        return (TextureRegistryOld) FUNCTIONS.get("getRegistry").apply(name);
    }

    static TextureRegistryOld getRegistry(Vanilla registry) {
        return getRegistry(registry.name());
    }

    static TextureRegistryOld currentRegistry() {
        return (TextureRegistryOld) SUPPLIERS.get("currentRegistry").get();
    }

    @SuppressWarnings("unchecked")
    static Collection<TextureRegistryOld> registries() {
        return (Collection<TextureRegistryOld>) SUPPLIERS.get("registries").get();
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
