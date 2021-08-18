package net.modificationstation.stationapi.api.client.texture.binder;

import net.minecraft.client.resource.TexturePack;

public interface TexturePackDependent {

    void refreshTextures(TexturePack newTexturePack);
}
