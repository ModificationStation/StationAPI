package net.modificationstation.stationapi.impl.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.Vec3i;
import net.modificationstation.stationapi.api.client.model.BakedModel;
import net.modificationstation.stationapi.api.client.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.model.Quad;
import net.modificationstation.stationapi.api.client.model.Vertex;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.client.texture.plugin.StationBlockRenderer;
import net.modificationstation.stationapi.mixin.render.client.BlockRendererAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

public class BakedModelRendererImpl implements BakedModelRenderer {

    private static final Direction[] DIRECTIONS = ObjectArrays.concat(Direction.values(), (Direction) null);

    @Nullable
    private final BlockRenderer blockRenderer;
    @Nullable
    private final BlockRendererAccessor blockRendererAccessor;
    @Nullable
    private final StationBlockRenderer stationBlockRenderer;
    private final LightingCalculatorImpl light = new LightingCalculatorImpl(3);
    private final Random random = new Random();
    private final Vec3i pos = new Vec3i();

    private Tessellator t;
    private final ObjIntConsumer<Vertex> renderVertexLight = this::renderVertexLight;
    private final Consumer<Vertex> renderVertexNormal = this::renderVertexNormal;

    public BakedModelRendererImpl(@Nullable BlockRenderer blockRenderer, @Nullable StationBlockRenderer stationBlockRenderer) {
        this.blockRenderer = blockRenderer;
        blockRendererAccessor = (BlockRendererAccessor) blockRenderer;
        this.stationBlockRenderer = stationBlockRenderer;
    }

    @Override
    public boolean renderWorld(BlockBase block, BakedModel model, BlockView blockView, int x, int y, int z) {
        if (blockRenderer == null || blockRendererAccessor == null || stationBlockRenderer == null)
            throw new NullPointerException("BlockRenderer is null!");
        else if (model == null)
            return false;
        else {
            pos.x = x;
            pos.y = y;
            pos.z = z;
            long seed = MathHelper.hashCode(x, y, z);
            int textureOverridePosition = blockRendererAccessor.getTextureOverride();
            Atlas.Sprite textureOverride = null;
            Atlas atlas;
            if (textureOverridePosition >= 0) {
                atlas = Atlases.getTerrain();
                textureOverride = atlas.getTexture(textureOverridePosition);
            } else
                atlas = model.getSprite().getAtlas();
            boolean noTextureOverride = textureOverride == null;
            boolean rendered = false;
            if (noTextureOverride) {
                t = stationBlockRenderer.prepareTessellator(atlas);
                int colourMultiplier = block.getColourMultiplier(blockView, x, y, z);
                float
                        colourMultiplierRed = (float) (colourMultiplier >> 16 & 255) / 255.0F,
                        colourMultiplierGreen = (float) (colourMultiplier >> 8 & 255) / 255.0F,
                        colourMultiplierBlue = (float) (colourMultiplier & 255) / 255.0F;
                if (GameRenderer.anaglyph3d) {
                    float
                            colourMultiplierGreenTmp = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 70.0F) / 100.0F,
                            colourMultiplierBlueTmp = (colourMultiplierRed * 30.0F + colourMultiplierBlue * 70.0F) / 100.0F;
                    colourMultiplierRed = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 59.0F + colourMultiplierBlue * 11.0F) / 100.0F;
                    colourMultiplierGreen = colourMultiplierGreenTmp;
                    colourMultiplierBlue = colourMultiplierBlueTmp;
                }
                light.initialize(
                        block,
                        blockView, x, y, z,
                        colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue,
                        Minecraft.isSmoothLightingEnabled() && model.useAmbientOcclusion()
                );
                Direction face;
                ImmutableList<Quad> quads;
                Quad q;
                for (int quadSet = 0, quadSetCount = DIRECTIONS.length; quadSet < quadSetCount; quadSet++) {
                    face = DIRECTIONS[quadSet];
                    random.setSeed(seed);
                    quads = model.getQuads(blockView, pos, face, random);
                    if (!quads.isEmpty() && (blockRendererAccessor.getRenderAllSides() || face == null || block.isSideRendered(blockView, x + face.vector.x, y + face.vector.y, z + face.vector.z, quadSet))) {
                        rendered = true;
                        for (int i = 0, quadsSize = quads.size(); i < quadsSize; i++) {
                            q = quads.get(i);
                            light.calculateForQuad(q);
                            q.applyToVertexesWithIndex(renderVertexLight);
                        }
                    }
                }
            }// else {
//                t = blockRendererCustomAccessor.getStationBlockRenderer().prepareTessellator(atlas);
//                int colourMultiplier = block.getColourMultiplier(blockView, x, y, z);
//                float
//                        colourMultiplierRed = (float) (colourMultiplier >> 16 & 255) / 255.0F,
//                        colourMultiplierGreen = (float) (colourMultiplier >> 8 & 255) / 255.0F,
//                        colourMultiplierBlue = (float) (colourMultiplier & 255) / 255.0F;
//                if (GameRenderer.anaglyph3d) {
//                    float
//                            colourMultiplierGreenTmp = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 70.0F) / 100.0F,
//                            colourMultiplierBlueTmp = (colourMultiplierRed * 30.0F + colourMultiplierBlue * 70.0F) / 100.0F;
//                    colourMultiplierRed = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 59.0F + colourMultiplierBlue * 11.0F) / 100.0F;
//                    colourMultiplierGreen = colourMultiplierGreenTmp;
//                    colourMultiplierBlue = colourMultiplierBlueTmp;
//                }
//                light.initialize(
//                        block,
//                        blockView, x, y, z,
//                        colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue,
//                        Minecraft.isSmoothLightingEnabled() && model.useAmbientOcclusion()
//                );
//                Direction face;
//                ImmutableList<Quad> quads;
//                Quad q;
//                for (int quadSet = 0, quadSetCount = DIRECTIONS.length; quadSet < quadSetCount; quadSet++) {
//                    face = DIRECTIONS[quadSet];
//                    random.setSeed(seed);
//                    quads = splitQuads(model.getQuads(blockView, pos, face, random));
//                    if (!quads.isEmpty() && (blockRendererAccessor.getRenderAllSides() || face == null || block.isSideRendered(blockView, x + face.vector.x, y + face.vector.y, z + face.vector.z, quadSet))) {
//                        rendered = true;
//                        for (int i = 0, quadsSize = quads.size(); i < quadsSize; i++) {
//                            q = quads.get(i);
//                            light.calculateForQuad(q);
//                            q.applyToVertexesWithIndex(renderVertexLight);
//                        }
//                    }
//                }
//            }
            return rendered;
        }
    }

//    private ImmutableList<Quad> splitQuads(ImmutableList<Quad> quads) {
//        if (quads.isEmpty())
//            return quads;
//        else {
//            ImmutableList.Builder<Quad> splitQuads = ImmutableList.builder();
//            for (int i = 0, size = quads.size(); i < size; i++) {
//                Quad q = quads.get(i);
//
//            }
//        }
//    }

    @Override
    public void renderInventory(BakedModel model) {
        if (model != null) {
            Atlas atlas = model.getSprite().getAtlas();
            if (stationBlockRenderer == null) {
                t = Tessellator.INSTANCE;
                t.start();
            } else
                t = stationBlockRenderer.prepareTessellator(atlas);
            Direction face;
            ImmutableList<Quad> quads;
            for (int vertexSet = 0, vertexSetCount = DIRECTIONS.length + 1; vertexSet < vertexSetCount; vertexSet++) {
                face = vertexSet < 6 ? DIRECTIONS[vertexSet] : null;
                random.setSeed(42);
                quads = model.getQuads(null, null, face, random);
                for (int i = 0, vertexesSize = quads.size(); i < vertexesSize; i++)
                    quads.get(i).applyToVertexes(renderVertexNormal);
            }
            t.draw();
        }
    }

    private void renderVertexLight(Vertex v, int i) {
        t.colour(light.get(i));
        t.vertex(pos.x + v.x, pos.y + v.y, pos.z + v.z, v.u, v.v);
    }

    private void renderVertexNormal(Vertex v) {
        t.setNormal(v.normalX, v.normalY, v.normalZ);
        t.vertex(v.x - .5, v.y - .5, v.z - .5, v.u, v.v);
    }
}
