package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.event.texture.TexturesPerFileChangedEvent;
import net.modificationstation.stationapi.api.common.StationAPI;
import org.lwjgl.opengl.GL11;

import java.util.*;

@Environment(EnvType.CLIENT)
public class TextureRegistry implements net.modificationstation.stationapi.api.client.texture.TextureRegistry {

    private static final Set<TextureRegistry> registries = new TreeSet<>();
    public static final TextureRegistry
            TERRAIN = new TextureRegistry("TERRAIN", "/terrain.png", 16, 16),
            PARTICLES = new TextureRegistry("PARTICLES", "/particles.png", 32, 32),
            GUI_ITEMS = new TextureRegistry("GUI_ITEMS", "/gui/items.png", 16, 16),
            GUI_PARTICLES = new TextureRegistry("GUI_PARTICLES", "/gui/particles.png", 32, 32);
    private static TextureRegistry currentRegistry;
    private static int nextRegistryID = 0;
    private final String name;
    private final int ordinal;
    private final Map<Integer, String> atlasIDToPath = new HashMap<>();
    private final Map<String, Integer> atlasPathToID = new HashMap<>();
    private int nextAtlasID;
    private short texturesInLine;
    private short texturesInColumn;
    private int currentTexture;

    public TextureRegistry(String name, String path, int texturesInLine, int texturesInColumn) {
        this.name = name;
        this.ordinal = nextRegistryID;
        registries.add(this);
        this.texturesInLine = (short) texturesInLine;
        this.texturesInColumn = (short) texturesInColumn;
        addAtlas(path);
        nextRegistryID++;
    }

    public static void unbind() {
        currentRegistry = null;
    }

    public static Object getRegistry(Object name) {
        for (net.modificationstation.stationapi.api.client.texture.TextureRegistry registry : registries())
            if (name.equals(registry.name()))
                return registry;
        return null;
    }

    public static net.modificationstation.stationapi.api.client.texture.TextureRegistry currentRegistry() {
        return currentRegistry;
    }

    public static Collection<net.modificationstation.stationapi.api.client.texture.TextureRegistry> registries() {
        return Collections.unmodifiableCollection(registries);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    @Override
    public int texturesInLine() {
        return Short.toUnsignedInt(texturesInLine);
    }

    @Override
    public int texturesInColumn() {
        return Short.toUnsignedInt(texturesInColumn);
    }

    @Override
    public int texturesPerFile() {
        return texturesInLine() * texturesInColumn();
    }

    @Override
    public String getAtlas(int ID) {
        return atlasIDToPath.get(ID);
    }

    @Override
    public Integer getAtlasID(String path) {
        return atlasPathToID.get(path);
    }

    @Override
    public int addAtlas(String atlas) {
        atlas = String.format(atlas, nextAtlasID);
        atlasPathToID.put(atlas, nextAtlasID);
        atlasIDToPath.put(nextAtlasID, atlas);
        int ret = nextAtlasID;
        nextAtlasID++;
        return ret;
    }

    @Override
    public void setTexturesInLine(int texturesInLine) {
        if (this.texturesInLine != (short) texturesInLine) {
            this.texturesInLine = (short) texturesInLine;
            StationAPI.EVENT_BUS.post(new TexturesPerFileChangedEvent(this));
        }
    }

    @Override
    public void setTexturesInColumn(int texturesInColumn) {
        if (this.texturesInColumn != (short) texturesInColumn) {
            this.texturesInColumn = (short) texturesInColumn;
            StationAPI.EVENT_BUS.post(new TexturesPerFileChangedEvent(this));
        }
    }

    @Override
    public int getAtlasTexture(TextureManager textureManager, int ID) {
        return textureManager.getTextureId(getAtlas(ID));
    }

    @Override
    public void bindAtlas(TextureManager textureManager, int ID) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getAtlasTexture(textureManager, ID));
        currentTexture = ID;
        if (currentRegistry != this)
            currentRegistry = this;
    }

    @Override
    public int currentTexture() {
        return currentTexture;
    }

    @Override
    public int compareTo(net.modificationstation.stationapi.api.client.texture.TextureRegistry o) {
        return -Integer.compare(ordinal(), o.ordinal());
    }

    @Override
    public String toString() {
        return name() + ", ordinal: " + ordinal() + ", atlases: " + Arrays.toString(atlasPathToID.values().toArray()) + ", textures in line: " + texturesInLine() + ", textures in column: " + texturesInColumn();
    }
}
