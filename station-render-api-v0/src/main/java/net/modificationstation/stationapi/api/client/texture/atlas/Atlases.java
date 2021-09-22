package net.modificationstation.stationapi.api.client.texture.atlas;

import net.modificationstation.stationapi.impl.client.texture.StationRenderAPI;

public final class Atlases {

    public static SquareAtlas getTerrain() {
        return StationRenderAPI.TERRAIN;
    }

    public static SquareAtlas getGuiItems() {
        return StationRenderAPI.GUI_ITEMS;
    }

    public static ExpandableAtlas getStationTerrain() {
        return StationRenderAPI.STATION_TERRAIN;
    }

    public static ExpandableAtlas getStationGuiItems() {
        return StationRenderAPI.STATION_GUI_ITEMS;
    }

    public static JsonModelAtlas getStationJsonModels() {
        return StationRenderAPI.STATION_JSON_MODELS;
    }
}
