package net.modificationstation.stationapi.api.client.world;

import net.modificationstation.stationapi.api.util.Util;

/**
 * Renderer's can implement this interface on worlds to give them access to sky brightness,
 * This currently isn't guaranteed to be implemented in arsenic, and isn't applied by default to worlds.
 * This however may change if station api ever adds lightmaps as a default renderer feature.
 */
public interface LightmapGetter {
    default float getSkyBrightness(float tickDelta) {
        return Util.assertImpl();
    }
}
