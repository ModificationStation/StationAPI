package net.modificationstation.stationapi.api.client.registry;

import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.Registry;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public final class AtlasRegistry extends Registry<Atlas> {

    public static final AtlasRegistry INSTANCE = new AtlasRegistry();

    private AtlasRegistry() {
        super(of(MODID, "atlases"));
    }
}
