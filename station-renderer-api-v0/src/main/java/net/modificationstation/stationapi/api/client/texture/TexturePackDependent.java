package net.modificationstation.stationapi.api.client.texture;

import net.minecraft.client.resource.pack.TexturePack;

public interface TexturePackDependent {

    void reloadFromTexturePack(TexturePack newTexturePack);
}
