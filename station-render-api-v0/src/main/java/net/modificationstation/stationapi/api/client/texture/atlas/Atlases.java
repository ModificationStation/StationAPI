package net.modificationstation.stationapi.api.client.texture.atlas;

import net.modificationstation.stationapi.impl.client.texture.TextureInit;

public class Atlases {

    public static SquareAtlas getTerrain() {
        return TextureInit.TERRAIN;
    }

    public static SquareAtlas getGuiItems() {
        return TextureInit.GUI_ITEMS;
    }

    public static ExpandableAtlas getStationTerrain() {
        return TextureInit.STATION_TERRAIN;
    }

    public static ExpandableAtlas getStationGuiItems() {
        return TextureInit.STATION_GUI_ITEMS;
    }

    public static JsonModelAtlas getStationJsonModels() {
        return TextureInit.STATION_JSON_MODELS;
    }
}
