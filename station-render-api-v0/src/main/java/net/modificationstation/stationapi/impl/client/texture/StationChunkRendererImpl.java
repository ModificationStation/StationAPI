package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TileRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.SquareAtlas;

import java.util.*;

public class StationChunkRendererImpl {

    public void renderAtlases(TileRenderer tileRenderer) {
        Set<Atlas> activeAtlases = ((StationBlockRendererProvider) tileRenderer).getStationBlockRenderer().activeAtlases;
        if (!activeAtlases.isEmpty()) {
            activeAtlases.forEach(atlas -> {
                atlas.bindAtlas();
                Tessellator tessellator = atlas.getTessellator();
                tessellator.draw();
                tessellator.setOffset(0, 0, 0);
            });
            activeAtlases.clear();
            SquareAtlas.TERRAIN.bindAtlas();
        }
    }
}
