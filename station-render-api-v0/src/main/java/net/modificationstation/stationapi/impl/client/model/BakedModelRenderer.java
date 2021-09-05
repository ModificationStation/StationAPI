package net.modificationstation.stationapi.impl.client.model;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.model.JsonModel;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.impl.client.texture.StationBlockRendererProvider;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

import java.util.*;

public class BakedModelRenderer {

    public static void renderWorld(BlockRenderer blockRenderer, BlockBase block, JsonModel model, BlockView blockView, int x, int y, int z) {
        if (model != null) {
            Atlas atlas = Atlases.getStationJsonModels();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            Tessellator tessellator = atlas.getTessellator();
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                ((StationBlockRendererProvider) blockRenderer).getStationBlockRenderer().activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            List<FaceQuadPoint> quadPoints = model.quadPoints;
            for (int i = 0, quadPointSize = quadPoints.size(); i < quadPointSize; i++) {
                FaceQuadPoint faceQuadPoint = quadPoints.get(i);
                float[] lighting = LightingHelper.getSmoothForQuadPoint(block, blockView, x, y, z, faceQuadPoint, i % 4);
                tessellator.colour(lighting[0], lighting[1], lighting[2]);
                tessellator.vertex(x + faceQuadPoint.x, y + faceQuadPoint.y, z + faceQuadPoint.z, faceQuadPoint.u, faceQuadPoint.v);
            }
        }
    }
}
