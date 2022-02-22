package net.modificationstation.stationapi.api.client.texture;

import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.client.render.StationTextureManagerImpl;

import java.util.*;

public interface StationTextureManager {

    static StationTextureManager get(TextureManager textureManager) {
        return StationTextureManagerImpl.get(textureManager);
    }

    void registerTexture(Identifier identifier, AbstractTexture texture);

    void bindTexture(Identifier id);

    Set<TextureTickListener> getTickListeners();
}
