package net.modificationstation.stationapi.api.client.render.material;

import net.modificationstation.stationapi.api.util.TriState;

public interface MaterialView {
    /**
     * @see MaterialFinder#emissive(boolean)
     */
    boolean emissive();

    /**
     * @see MaterialFinder#disableDiffuse(boolean)
     */
    boolean disableDiffuse();

    /**
     * @see MaterialFinder#ambientOcclusion(TriState)
     */
    TriState ambientOcclusion();

    /**
     * @see MaterialFinder#shadeMode(ShadeMode)
     */
    ShadeMode shadeMode();
}
