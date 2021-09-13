package net.modificationstation.stationapi.impl.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.Direction;
import net.modificationstation.stationapi.api.client.model.BakedModel;
import net.modificationstation.stationapi.api.client.model.Vertex;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.impl.client.texture.StationBlockRendererProvider;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

public class BakedModelRenderer {

    public static void renderWorld(BlockRenderer blockRenderer, BlockBase block, BakedModel model, BlockView blockView, int x, int y, int z) {
        if (model != null) {
            Atlas atlas = model.getAtlas();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            Tessellator tessellator = atlas.getTessellator();
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                ((StationBlockRendererProvider) blockRenderer).getStationBlockRenderer().activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            for (int vertexSet = 0; vertexSet < 7; vertexSet++) {
                ImmutableList<Vertex> vertexes;
                if (vertexSet == 6)
                    vertexes = model.vertexes;
                else {
                    Direction face = Direction.values()[vertexSet];
                    vertexes = model.faceVertexes.get(face);
                    if (!block.isSideRendered(blockView, x + face.vector.x, y + face.vector.y, z + face.vector.z, vertexSet))
                        continue;
                }
                Vertex vertex;
                for (int i = 0, vertexesSize = vertexes.size(); i < vertexesSize; i++) {
                    vertex = vertexes.get(i);
                    tessellator.colour(LightingHelper.getSmoothForQuadPoint(block, blockView, x, y, z, vertex, i % 4));
                    tessellator.vertex(x + vertex.x, y + vertex.y, z + vertex.z, vertex.u, vertex.v);
                }
            }
        }
    }

    public static void renderInventory(BakedModel model) {
        if (model != null) {
            Atlas atlas = model.getAtlas();
            Tessellator tessellator = atlas.getTessellator();
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                tessellator.start();
                atlas.bindAtlas();
            }
            for (int vertexSet = 0; vertexSet < 7; vertexSet++) {
                ImmutableList<Vertex> vertexes = vertexSet == 6 ? model.vertexes : model.faceVertexes.get(Direction.values()[vertexSet]);
                Vertex vertex;
                for (int i = 0, vertexesSize = vertexes.size(); i < vertexesSize; i++) {
                    vertex = vertexes.get(i);
                    tessellator.setNormal(vertex.normalX, vertex.normalY, vertex.normalZ);
                    tessellator.vertex(vertex.x - .5, vertex.y - .5, vertex.z - .5, vertex.u, vertex.v);
                }
            }
            tessellator.draw();
        }
    }
}
