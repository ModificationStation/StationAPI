package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.impl.client.model.BakedModelRenderer;

public interface BlockInventoryModelProvider extends BlockWithInventoryRenderer {

    /**
     * Model to render inside the inventory.
     */
    Model getInventoryModel(int meta);

    @Override
    default void renderInventory(BlockRenderer blockRenderer, int meta) {
        BakedModelRenderer.renderInventory(getInventoryModel(meta).getBaked());
//        JsonModel model = getInventoryModel(meta);
//        if (model != null) {
//            Tessellator tessellator = Atlases.getStationJsonModels().getTessellator();
//            tessellator.start();
//            Atlases.getStationJsonModels().bindAtlas();
//            for (JsonCuboidData cuboid : model.getCuboids()) {
//                double[]
//                        from = cuboid.from,
//                        to = cuboid.to;
//                JsonFacesData jsonFacesData = cuboid.faces;
//                JsonFaceData jsonFaceData;
//                double[] uv;
//                jsonFaceData = jsonFacesData.down;
//                tessellator.setNormal(0F, -1F, 0F);
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(from[0] - .5, from[1] - .5, to[2] -.5, uv[0], uv[3]);
//                tessellator.vertex(from[0] - .5, from[1] - .5, from[2] -.5, uv[0], uv[1]);
//                tessellator.vertex(to[0] - .5, from[1] - .5, from[2] -.5, uv[2], uv[1]);
//                tessellator.vertex(to[0] - .5, from[1] - .5, to[2] -.5, uv[2], uv[3]);
//                jsonFaceData = jsonFacesData.up;
//                tessellator.setNormal(0F, 1F, 0F);
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(to[0] - .5, to[1] - .5, to[2] -.5, uv[2], uv[3]);
//                tessellator.vertex(to[0] - .5, to[1] - .5, from[2] -.5, uv[2], uv[1]);
//                tessellator.vertex(from[0] - .5, to[1] - .5, from[2] -.5, uv[0], uv[1]);
//                tessellator.vertex(from[0] - .5, to[1] - .5, to[2] -.5, uv[0], uv[3]);
//                jsonFaceData = jsonFacesData.east;
//                tessellator.setNormal(0F, 0F, -1F);
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(from[0] - .5, to[1] - .5, from[2] -.5, uv[2], uv[1]);
//                tessellator.vertex(to[0] - .5, to[1] - .5, from[2] -.5, uv[0], uv[1]);
//                tessellator.vertex(to[0] - .5, from[1] - .5, from[2] -.5, uv[0], uv[3]);
//                tessellator.vertex(from[0] - .5, from[1] - .5, from[2] -.5, uv[2], uv[3]);
//                jsonFaceData = jsonFacesData.west;
//                tessellator.setNormal(0F, 0F, 1F);
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(from[0] - .5, to[1] - .5, to[2] -.5, uv[0], uv[1]);
//                tessellator.vertex(from[0] - .5, from[1] - .5, to[2] -.5, uv[0], uv[3]);
//                tessellator.vertex(to[0] - .5, from[1] - .5, to[2] -.5, uv[2], uv[3]);
//                tessellator.vertex(to[0] - .5, to[1] - .5, to[2] -.5, uv[2], uv[1]);
//                jsonFaceData = jsonFacesData.north;
//                tessellator.setNormal(-1F, 0F, 0F);
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(from[0] - .5, to[1] - .5, to[2] -.5, uv[2], uv[1]);
//                tessellator.vertex(from[0] - .5, to[1] - .5, from[2] -.5, uv[0], uv[1]);
//                tessellator.vertex(from[0] - .5, from[1] - .5, from[2] -.5, uv[0], uv[3]);
//                tessellator.vertex(from[0] - .5, from[1] - .5, to[2] -.5, uv[2], uv[3]);
//                jsonFaceData = jsonFacesData.south;
//                tessellator.setNormal(1F, 0F, 0F);
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(to[0] - .5, from[1] - .5, to[2] -.5, uv[0], uv[3]);
//                tessellator.vertex(to[0] - .5, from[1] - .5, from[2] -.5, uv[2], uv[3]);
//                tessellator.vertex(to[0] - .5, to[1] - .5, from[2] -.5, uv[2], uv[1]);
//                tessellator.vertex(to[0] - .5, to[1] - .5, to[2] -.5, uv[0], uv[1]);
//            }
//            tessellator.draw();
//        }
    }
}
