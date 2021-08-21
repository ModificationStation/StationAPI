package net.modificationstation.stationapi.api.client.model;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.JsonModelAtlas;
import net.modificationstation.stationapi.impl.client.model.JsonCuboidData;
import net.modificationstation.stationapi.impl.client.model.JsonFaceData;
import net.modificationstation.stationapi.impl.client.model.JsonFacesData;

public interface BlockInventoryModelProvider extends BlockWithInventoryRenderer {

    /**
     * Model to render inside the inventory.
     */
    JsonModel getInventoryModel(int meta);

    @Override
    default void renderInventory(BlockRenderer blockRenderer, int meta) {
        JsonModel model = getInventoryModel(meta);
        if (model != null) {
            Tessellator tessellator = JsonModelAtlas.STATION_JSON_MODELS.getTessellator();
            tessellator.start();
            JsonModelAtlas.STATION_JSON_MODELS.bindAtlas();
            for (JsonCuboidData cuboid : model.getCuboids()) {
                double[]
                        from = cuboid.getFrom(),
                        to = cuboid.getTo();
                JsonFacesData jsonFacesData = cuboid.getFaces();
                JsonFaceData jsonFaceData;
                double[] uv;
                jsonFaceData = jsonFacesData.getDown();
                tessellator.setNormal(0F, -1F, 0F);
                uv = jsonFaceData.getUv();
                tessellator.vertex(from[0] / 16 - .5, from[1] / 16 - .5, to[2] / 16 -.5, uv[0], uv[3]);
                tessellator.vertex(from[0] / 16 - .5, from[1] / 16 - .5, from[2] / 16 -.5, uv[0], uv[1]);
                tessellator.vertex(to[0] / 16 - .5, from[1] / 16 - .5, from[2] / 16 -.5, uv[2], uv[1]);
                tessellator.vertex(to[0] / 16 - .5, from[1] / 16 - .5, to[2] / 16 -.5, uv[2], uv[3]);
                jsonFaceData = jsonFacesData.getUp();
                tessellator.setNormal(0F, 1F, 0F);
                uv = jsonFaceData.getUv();
                tessellator.vertex(to[0] / 16 - .5, to[1] / 16 - .5, to[2] / 16 -.5, uv[2], uv[3]);
                tessellator.vertex(to[0] / 16 - .5, to[1] / 16 - .5, from[2] / 16 -.5, uv[2], uv[1]);
                tessellator.vertex(from[0] / 16 - .5, to[1] / 16 - .5, from[2] / 16 -.5, uv[0], uv[1]);
                tessellator.vertex(from[0] / 16 - .5, to[1] / 16 - .5, to[2] / 16 -.5, uv[0], uv[3]);
                jsonFaceData = jsonFacesData.getEast();
                tessellator.setNormal(0F, 0F, -1F);
                uv = jsonFaceData.getUv();
                // actually south
                tessellator.vertex(to[0] / 16 - .5, from[1] / 16 - .5, to[2] / 16 -.5, uv[0], uv[3]);
                tessellator.vertex(to[0] / 16 - .5, from[1] / 16 - .5, from[2] / 16 -.5, uv[2], uv[3]);
                tessellator.vertex(to[0] / 16 - .5, to[1] / 16 - .5, from[2] / 16 -.5, uv[2], uv[1]);
                tessellator.vertex(to[0] / 16 - .5, to[1] / 16 - .5, to[2] / 16 -.5, uv[0], uv[1]);
                jsonFaceData = jsonFacesData.getWest();
                tessellator.setNormal(0F, 0F, 1F);
                uv = jsonFaceData.getUv();
                // actually north
                tessellator.vertex(from[0] / 16 - .5, to[1] / 16 - .5, to[2] / 16 -.5, uv[2], uv[1]);
                tessellator.vertex(from[0] / 16 - .5, to[1] / 16 - .5, from[2] / 16 -.5, uv[0], uv[1]);
                tessellator.vertex(from[0] / 16 - .5, from[1] / 16 - .5, from[2] / 16 -.5, uv[0], uv[3]);
                tessellator.vertex(from[0] / 16 - .5, from[1] / 16 - .5, to[2] / 16 -.5, uv[2], uv[3]);
                jsonFaceData = jsonFacesData.getNorth();
                tessellator.setNormal(-1F, 0F, 0F);
                uv = jsonFaceData.getUv();
                // actually west
                tessellator.vertex(from[0] / 16 - .5, to[1] / 16 - .5, to[2] / 16 -.5, uv[0], uv[1]);
                tessellator.vertex(from[0] / 16 - .5, from[1] / 16 - .5, to[2] / 16 -.5, uv[0], uv[3]);
                tessellator.vertex(to[0] / 16 - .5, from[1] / 16 - .5, to[2] / 16 -.5, uv[2], uv[3]);
                tessellator.vertex(to[0] / 16 - .5, to[1] / 16 - .5, to[2] / 16 -.5, uv[2], uv[1]);
                jsonFaceData = jsonFacesData.getSouth();
                tessellator.setNormal(1F, 0F, 0F);
                uv = jsonFaceData.getUv();
                // actually east
                tessellator.vertex(from[0] / 16 - .5, to[1] / 16 - .5, from[2] / 16 -.5, uv[2], uv[1]);
                tessellator.vertex(to[0] / 16 - .5, to[1] / 16 - .5, from[2] / 16 -.5, uv[0], uv[1]);
                tessellator.vertex(to[0] / 16 - .5, from[1] / 16 - .5, from[2] / 16 -.5, uv[0], uv[3]);
                tessellator.vertex(from[0] / 16 - .5, from[1] / 16 - .5, from[2] / 16 -.5, uv[2], uv[3]);
            }
            tessellator.draw();
        }
    }
}
