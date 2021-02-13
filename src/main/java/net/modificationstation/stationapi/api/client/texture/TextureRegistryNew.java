package net.modificationstation.stationapi.api.client.texture;

import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.Registry;

public class TextureRegistryNew extends Registry<TextureSet> {

    private TextureRegistryNew(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }

    public static final TextureRegistryNew INSTANCE = new TextureRegistryNew(Identifier.of(StationAPI.MODID, "texture_sets"));
}
