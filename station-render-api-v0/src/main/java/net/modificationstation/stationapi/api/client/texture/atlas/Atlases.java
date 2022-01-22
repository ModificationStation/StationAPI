package net.modificationstation.stationapi.api.client.texture.atlas;

import net.modificationstation.stationapi.impl.client.texture.StationRenderAPI;

public final class Atlases {

    public static ExpandableAtlas getTerrain() {
        return StationRenderAPI.TERRAIN;
    }

    public static ExpandableAtlas getGuiItems() {
        return StationRenderAPI.GUI_ITEMS;
    }

    /* !==========================! */
    /* !--- DEPRECATED SECTION ---! */
    /* !==========================! */

    @Deprecated
    public static ExpandableAtlas getStationTerrain() {
        return getTerrain();
    }

    @Deprecated
    public static ExpandableAtlas getStationGuiItems() {
        return getGuiItems();
    }

    @Deprecated
    public static ExpandableAtlas getStationJsonModels() {
        return getTerrain();
    }
}
