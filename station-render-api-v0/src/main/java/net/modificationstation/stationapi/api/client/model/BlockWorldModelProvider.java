package net.modificationstation.stationapi.api.client.model;

import net.minecraft.client.render.QuadPoint;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.impl.client.texture.StationBlockRendererProvider;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

import java.util.*;

public interface BlockWorldModelProvider extends BlockWithWorldRenderer {

    /**
     * The model to render in the world.
     */
    JsonModel getCustomWorldModel(BlockView blockView, int x, int y, int z);

    @Override
    default void renderWorld(BlockRenderer blockRenderer, BlockView blockView, int x, int y, int z) {
        JsonModel model = getCustomWorldModel(blockView, x, y, z);
        if (model != null) {
            Atlas atlas = Atlases.getStationJsonModels();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            Tessellator tessellator = Atlases.getStationJsonModels().getTessellator();
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                ((StationBlockRendererProvider) blockRenderer).getStationBlockRenderer().activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            if (originalAccessor.getHasColour())
                tessellator.colour(originalAccessor.getColour());
//            for (JsonCuboidData cuboid : model.getCuboids()) {
//                double[]
//                        from = cuboid.from,
//                        to = cuboid.to;
//                double
//                        xFrom = x + from[0],
//                        yFrom = y + from[1],
//                        zFrom = z + from[2],
//                        xTo = x + to[0],
//                        yTo = y + to[1],
//                        zTo = z + to[2];
//                JsonFacesData jsonFacesData = cuboid.faces;
//                JsonFaceData jsonFaceData;
//                double[] uv;
//                jsonFaceData = jsonFacesData.down;
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(xFrom, yFrom, zTo, uv[0], uv[3]);
//                tessellator.vertex(xFrom, yFrom, zFrom, uv[0], uv[1]);
//                tessellator.vertex(xTo, yFrom, zFrom, uv[2], uv[1]);
//                tessellator.vertex(xTo, yFrom, zTo, uv[2], uv[3]);
//                jsonFaceData = jsonFacesData.up;
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(xTo, yTo, zTo, uv[2], uv[3]);
//                tessellator.vertex(xTo, yTo, zFrom, uv[2], uv[1]);
//                tessellator.vertex(xFrom, yTo, zFrom, uv[0], uv[1]);
//                tessellator.vertex(xFrom, yTo, zTo, uv[0], uv[3]);
//                jsonFaceData = jsonFacesData.east;
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(xFrom, yTo, zFrom, uv[2], uv[1]);
//                tessellator.vertex(xTo, yTo, zFrom, uv[0], uv[1]);
//                tessellator.vertex(xTo, yFrom, zFrom, uv[0], uv[3]);
//                tessellator.vertex(xFrom, yFrom, zFrom, uv[2], uv[3]);
//                jsonFaceData = jsonFacesData.west;
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(xFrom, yTo, zTo, uv[0], uv[1]);
//                tessellator.vertex(xFrom, yFrom, zTo, uv[0], uv[3]);
//                tessellator.vertex(xTo, yFrom, zTo, uv[2], uv[3]);
//                tessellator.vertex(xTo, yTo, zTo, uv[2], uv[1]);
//                jsonFaceData = jsonFacesData.north;
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(xFrom, yTo, zTo, uv[2], uv[1]);
//                tessellator.vertex(xFrom, yTo, zFrom, uv[0], uv[1]);
//                tessellator.vertex(xFrom, yFrom, zFrom, uv[0], uv[3]);
//                tessellator.vertex(xFrom, yFrom, zTo, uv[2], uv[3]);
//                jsonFaceData = jsonFacesData.south;
//                uv = jsonFaceData.getUv();
//                tessellator.vertex(xTo, yFrom, zTo, uv[0], uv[3]);
//                tessellator.vertex(xTo, yFrom, zFrom, uv[2], uv[3]);
//                tessellator.vertex(xTo, yTo, zFrom, uv[2], uv[1]);
//                tessellator.vertex(xTo, yTo, zTo, uv[0], uv[1]);
//            }
            List<QuadPoint> quads = model.quads;
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0, quadsSize = quads.size(); i < quadsSize; i++) {
                QuadPoint quad = quads.get(i);
                Vec3f pos = quad.pointVector;
                tessellator.vertex(x + pos.x, y + pos.y, z + pos.z, quad.field_1147, quad.field_1148);
            }
        }
    }
}
