package net.modificationstation.stationapi.api.client.texture.atlas;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Deprecated
@Environment(EnvType.CLIENT)
public interface CustomAtlasProvider {

    @Deprecated
    @Environment(EnvType.CLIENT)
    Atlas getAtlas();
}
