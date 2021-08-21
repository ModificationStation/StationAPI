package net.modificationstation.stationapi.api.client.model;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.JsonModelAtlas;
import net.modificationstation.stationapi.impl.client.model.JsonCuboidData;
import net.modificationstation.stationapi.impl.client.model.JsonFaceData;
import net.modificationstation.stationapi.impl.client.model.JsonFacesData;
import net.modificationstation.stationapi.impl.client.texture.StationBlockRendererProvider;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

public interface BlockWorldModelProvider extends BlockWithWorldRenderer {

    /**
     * The model to render in the world.
     */
    JsonModel getCustomWorldModel(BlockView blockView, int x, int y, int z);

    @Override
    default void renderWorld(BlockRenderer blockRenderer, BlockView blockView, int x, int y, int z) {
        JsonModel model = getCustomWorldModel(blockView, x, y, z);
        if (model != null) {
            Atlas atlas = JsonModelAtlas.STATION_JSON_MODELS;
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            Tessellator tessellator = JsonModelAtlas.STATION_JSON_MODELS.getTessellator();
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                ((StationBlockRendererProvider) blockRenderer).getStationBlockRenderer().activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            if (originalAccessor.getHasColour())
                tessellator.colour(originalAccessor.getColour());
            for (JsonCuboidData cuboid : model.getCuboids()) {
                double[]
                        from = cuboid.getFrom(),
                        to = cuboid.getTo();
                JsonFacesData jsonFacesData = cuboid.getFaces();
                JsonFaceData jsonFaceData;
                double[] uv;
                jsonFaceData = jsonFacesData.getDown();
                uv = jsonFaceData.getUv();
                tessellator.vertex(x + from[0] / 16, y + from[1] / 16, z + to[2] / 16, uv[0], uv[3]);
                tessellator.vertex(x + from[0] / 16, y + from[1] / 16, z + from[2] / 16, uv[0], uv[1]);
                tessellator.vertex(x + to[0] / 16, y + from[1] / 16, z + from[2] / 16, uv[2], uv[1]);
                tessellator.vertex(x + to[0] / 16, y + from[1] / 16, z + to[2] / 16, uv[2], uv[3]);
                jsonFaceData = jsonFacesData.getUp();
                uv = jsonFaceData.getUv();
                tessellator.vertex(x + to[0] / 16, y + to[1] / 16, z + to[2] / 16, uv[2], uv[3]);
                tessellator.vertex(x + to[0] / 16, y + to[1] / 16, z + from[2] / 16, uv[2], uv[1]);
                tessellator.vertex(x + from[0] / 16, y + to[1] / 16, z + from[2] / 16, uv[0], uv[1]);
                tessellator.vertex(x + from[0] / 16, y + to[1] / 16, z + to[2] / 16, uv[0], uv[3]);
                jsonFaceData = jsonFacesData.getEast();
                uv = jsonFaceData.getUv();
                // actually south
                tessellator.vertex(x + to[0] / 16, y + from[1] / 16, z + to[2] / 16, uv[0], uv[3]);
                tessellator.vertex(x + to[0] / 16, y + from[1] / 16, z + from[2] / 16, uv[2], uv[3]);
                tessellator.vertex(x + to[0] / 16, y + to[1] / 16, z + from[2] / 16, uv[2], uv[1]);
                tessellator.vertex(x + to[0] / 16, y + to[1] / 16, z + to[2] / 16, uv[0], uv[1]);
                jsonFaceData = jsonFacesData.getWest();
                uv = jsonFaceData.getUv();
                // actually north
                tessellator.vertex(x + from[0] / 16, y + to[1] / 16, z + to[2] / 16, uv[2], uv[1]);
                tessellator.vertex(x + from[0] / 16, y + to[1] / 16, z + from[2] / 16, uv[0], uv[1]);
                tessellator.vertex(x + from[0] / 16, y + from[1] / 16, z + from[2] / 16, uv[0], uv[3]);
                tessellator.vertex(x + from[0] / 16, y + from[1] / 16, z + to[2] / 16, uv[2], uv[3]);
                jsonFaceData = jsonFacesData.getNorth();
                uv = jsonFaceData.getUv();
                // actually west
                tessellator.vertex(x + from[0] / 16, y + to[1] / 16, z + to[2] / 16, uv[0], uv[1]);
                tessellator.vertex(x + from[0] / 16, y + from[1] / 16, z + to[2] / 16, uv[0], uv[3]);
                tessellator.vertex(x + to[0] / 16, y + from[1] / 16, z + to[2] / 16, uv[2], uv[3]);
                tessellator.vertex(x + to[0] / 16, y + to[1] / 16, z + to[2] / 16, uv[2], uv[1]);
                jsonFaceData = jsonFacesData.getSouth();
                uv = jsonFaceData.getUv();
                // actually east
                tessellator.vertex(x + from[0] / 16, y + to[1] / 16, z + from[2] / 16, uv[2], uv[1]);
                tessellator.vertex(x + to[0] / 16, y + to[1] / 16, z + from[2] / 16, uv[0], uv[1]);
                tessellator.vertex(x + to[0] / 16, y + from[1] / 16, z + from[2] / 16, uv[0], uv[3]);
                tessellator.vertex(x + from[0] / 16, y + from[1] / 16, z + from[2] / 16, uv[2], uv[3]);
            }
        }
    }
}
