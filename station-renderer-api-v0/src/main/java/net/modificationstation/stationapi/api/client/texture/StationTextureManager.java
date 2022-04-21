package net.modificationstation.stationapi.api.client.texture;

import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.client.render.StationTextureManagerImpl;

import java.util.Set;

public interface StationTextureManager {

    AbstractTexture getTexture(Identifier id);

    Identifier registerDynamicTexture(String prefix, NativeImageBackedTexture texture);

    static StationTextureManager get(TextureManager textureManager) {
        return StationTextureManagerImpl.get(textureManager);
    }

    void registerTexture(Identifier identifier, AbstractTexture texture);

    void bindTexture(Identifier id);

    Set<TextureTickListener> getTickListeners();
}
