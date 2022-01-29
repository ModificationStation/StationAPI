package net.modificationstation.stationapi.api.client.texture.atlas;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.client.texture.StationRenderAPI;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

public final class Atlases {

    public static final Identifier
            BLOCK_ATLAS_TEXTURE = of("terrain"),
            ITEM_ATLAS_TEXTURE = of("gui_items");

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
