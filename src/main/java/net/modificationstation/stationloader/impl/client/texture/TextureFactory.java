package net.modificationstation.stationloader.impl.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_214;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationloader.mixin.client.accessor.TextureManagerAccessor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Environment(EnvType.CLIENT)
public class TextureFactory implements net.modificationstation.stationloader.api.client.texture.TextureFactory {

    @Override
    public int addTexture(net.modificationstation.stationloader.api.client.texture.TextureRegistry type, String pathToImage) {
        StaticTexture texture = new StaticTexture(type, pathToImage);
        try {
            setupTexture(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, texture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        textures = Arrays.copyOf(textures, textures.length + 1);
        textures[textures.length - 1] = texture;
        return texture.atlasID * type.texturesPerFile() + texture.index;
    }

    private void setupTexture(TextureManager textureManager, StaticTexture texture) throws IOException {
        texture.prepareTexture();
        textureManager.add(texture);
    }

    @Override
    public int createNewAtlas(net.modificationstation.stationloader.api.client.texture.TextureRegistry type, String originalAtlas, String path) {
        int ret = type.addAtlas(String.format(path, type.name().toLowerCase(), "%d"));
        createAtlasCopy(type, originalAtlas, ret, path);
        return ret;
    }

    @Override
    public int createAtlasCopy(net.modificationstation.stationloader.api.client.texture.TextureRegistry type, String originalAtlas, int ID, String path) {
        originalAtlas = String.format(originalAtlas, type.name().toLowerCase());
        path = String.format(path, type.name().toLowerCase(), ID);
        Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
        TexturePack texturePack = mc.texturePackManager.texturePack;
        TextureManager textureManager = mc.textureManager;
        Map<String, Integer> textureMap = ((TextureManagerAccessor) textureManager).getField_1246();
        IntBuffer singleIntBuffer = ((TextureManagerAccessor) textureManager).getField_1249();
        BufferedImage missingTextureImage = ((TextureManagerAccessor) textureManager).getField_1257();
        singleIntBuffer.clear();
        class_214.method_742(singleIntBuffer);
        int var6 = singleIntBuffer.get(0);
        InputStream var7 = texturePack.method_976(originalAtlas);
        if (var7 == null)
            textureManager.method_1089(missingTextureImage, var6);
        else
            textureManager.method_1089(((TextureManagerAccessor) textureManager).invokeMethod_1091(var7), var6);
        textureMap.put(path, var6);
        return var6;
    }

    @Override
    public int nextSpriteID(net.modificationstation.stationloader.api.client.texture.TextureRegistry type) {
        if (!spriteIDs.containsKey(type)) {
            TreeMap<Integer, Integer> treeMap = new TreeMap<>();
            int atlasID = createNewAtlas(type, stationOriginalAtlas, stationCopiedAtlas);
            treeMap.put(atlasID, 0);
            spriteIDs.put(type, treeMap);
        }
        TreeMap<Integer, Integer> atlases = spriteIDs.get(type);
        if (atlases.get(atlases.lastKey()) >= type.texturesPerFile())
            atlases.put(createNewAtlas(type, stationOriginalAtlas, stationCopiedAtlas), 0);
        int atlasID = atlases.lastKey();
        int spriteID = atlasID * type.texturesPerFile() + atlases.get(atlasID);
        atlases.put(atlasID, atlases.get(atlasID) + 1);
        return spriteID;
    }

    public static String stationOriginalAtlas = "/assets/stationloader/atlases/station.%s.png";
    public static String stationCopiedAtlas = "/assets/stationloader/atlases/station.%s.%s.png";
    private static StaticTexture[] textures = new StaticTexture[0];
    private static final Map<net.modificationstation.stationloader.api.client.texture.TextureRegistry, TreeMap<Integer, Integer>> spriteIDs = new HashMap<>();
}
