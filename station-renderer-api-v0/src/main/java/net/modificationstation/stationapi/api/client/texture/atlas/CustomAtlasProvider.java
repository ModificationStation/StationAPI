package net.modificationstation.stationapi.api.client.texture.atlas;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface CustomAtlasProvider {

    @Environment(EnvType.CLIENT)
    Atlas getAtlas();
}
