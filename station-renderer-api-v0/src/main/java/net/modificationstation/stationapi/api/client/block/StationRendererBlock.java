package net.modificationstation.stationapi.api.client.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.util.Util;

@EnvironmentInterface(value = EnvType.CLIENT, itf = CustomAtlasProvider.class)
public interface StationRendererBlock extends CustomAtlasProvider {

    @Override
    @Environment(EnvType.CLIENT)
    default Atlas getAtlas() {
        return Util.assertImpl();
    }
}
