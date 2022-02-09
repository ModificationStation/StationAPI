package net.modificationstation.stationapi.impl.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Ints;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateView;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.client.texture.plugin.TessellatorPlugin;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.client.texture.FastTessellator;
import net.modificationstation.stationapi.impl.client.texture.plugin.StationBlockRenderer;
import net.modificationstation.stationapi.mixin.render.TilePosAccessor;
import net.modificationstation.stationapi.mixin.render.client.BlockRendererAccessor;

import java.util.*;

public class BakedModelRendererImpl implements BakedModelRenderer {

    private static final Direction[] DIRECTIONS = ObjectArrays.concat(Direction.values(), (Direction) null);

    private final BlockRendererAccessor blockRendererAccessor;
    private final StationBlockRenderer stationBlockRenderer;
    private final LightingCalculatorImpl light = new LightingCalculatorImpl(3);
    private final Random random = new Random();
    private final TilePos pos = new TilePos(0, 0, 0);
    private final TilePosAccessor posAccessor = (TilePosAccessor) pos;

    public BakedModelRendererImpl(BlockRenderer blockRenderer, StationBlockRenderer stationBlockRenderer) {
        blockRendererAccessor = (BlockRendererAccessor) blockRenderer;
        this.stationBlockRenderer = stationBlockRenderer;
    }

    @Override
    public boolean renderWorld(BlockBase block, BakedModel model, BlockView blockView, int x, int y, int z) {
        posAccessor.stationapi$setX(x);
        posAccessor.stationapi$setY(y);
        posAccessor.stationapi$setZ(z);
        long seed = MathHelper.hashCode(x, y, z);
        if (blockRendererAccessor.getTextureOverride() >= 0)
            return true;
        Tessellator t = Tessellator.INSTANCE;
        FastTessellator fastT = ((TessellatorPlugin.Provider) t).getPlugin();
//        int colourMultiplier = block.getColourMultiplier(blockView, x, y, z);
//        float
//                colourMultiplierRed = (float) (colourMultiplier >> 16 & 255) / 255.0F,
//                colourMultiplierGreen = (float) (colourMultiplier >> 8 & 255) / 255.0F,
//                colourMultiplierBlue = (float) (colourMultiplier & 255) / 255.0F;
//        if (GameRenderer.anaglyph3d) {
//            float
//                    colourMultiplierGreenTmp = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 70.0F) / 100.0F,
//                    colourMultiplierBlueTmp = (colourMultiplierRed * 30.0F + colourMultiplierBlue * 70.0F) / 100.0F;
//            colourMultiplierRed = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 59.0F + colourMultiplierBlue * 11.0F) / 100.0F;
//            colourMultiplierGreen = colourMultiplierGreenTmp;
//            colourMultiplierBlue = colourMultiplierBlueTmp;
//        }
        light.initialize(
                block,
                blockView, x, y, z,
                Minecraft.isSmoothLightingEnabled() && model.useAmbientOcclusion()
        );
        boolean rendered = false;
        ImmutableList<BakedQuad> qs;
        BakedQuad q;
        float[] qlight;
        BlockState state = ((BlockStateView) blockView).getBlockState(x, y, z);
        for (int quadSet = 0, size = DIRECTIONS.length; quadSet < size; quadSet++) {
            Direction face = DIRECTIONS[quadSet];
            random.setSeed(seed);
            qs = model.getQuads(blockView, pos, face, random);
            if (!qs.isEmpty() && (face == null || block.isSideRendered(blockView, x + face.vector.x, y + face.vector.y, z + face.vector.z, quadSet))) {
                rendered = true;
                for (int j = 0, quadSize = qs.size(); j < quadSize; j++) {
                    q = qs.get(j);
                    qlight = light.calculateForQuad(q);
                    if (q.hasColor()) {
                        int i = StationRenderAPI.BAKED_MODEL_MANAGER.colourMap.getColour(state, blockView, pos, q.getColorIndex());
                        float r = redI2F(i);
                        float g = greenI2F(i);
                        float b = blueI2F(i);
                        if (GameRenderer.anaglyph3d) {
                            float
                                    colourMultiplierGreenTmp = (r * 30F + g * 70F) / 100F,
                                    colourMultiplierBlueTmp = (r * 30F + b * 70F) / 100F;
                            r = (r * 30F + g * 59F + b * 11F) / 100F;
                            g = colourMultiplierGreenTmp;
                            b = colourMultiplierBlueTmp;
                        }
                        fastT.quad(q.getVertexData(), x, y, z,
                                colourF2I(r * qlight[0], g * qlight[0], b * qlight[0]),
                                colourF2I(r * qlight[1], g * qlight[1], b * qlight[1]),
                                colourF2I(r * qlight[2], g * qlight[2], b * qlight[2]),
                                colourF2I(r * qlight[3], g * qlight[3], b * qlight[3])
                        );
                    } else
                        fastT.quad(q.getVertexData(), x, y, z,
                                colourF2I(qlight[0], qlight[0], qlight[0]),
                                colourF2I(qlight[1], qlight[1], qlight[1]),
                                colourF2I(qlight[2], qlight[2], qlight[2]),
                                colourF2I(qlight[3], qlight[3], qlight[3])
                        );
                }
            }
        }
        return rendered;
    }

    private float redI2F(int colour) {
        return ((colour >> 16) & 255) / 255F;
    }

    private float greenI2F(int colour) {
        return ((colour >> 8) & 255) / 255F;
    }

    private float blueI2F(int colour) {
        return (colour & 255) / 255F;
    }

    private int colourF2I(float r, float g, float b) {
        return (255 << 24) | (colourChannelF2I(r) << 16) | (colourChannelF2I(g) << 8) | colourChannelF2I(b);
    }

    private int colourChannelF2I(float colourChannel) {
        return Ints.constrainToRange((int) (colourChannel * 255), 0, 255);
    }

    @Override
    public void renderInventory(BakedModel model) {
//        if (model != null) {
//            Atlas atlas = model.getSprite().getAtlas();
//            t = stationBlockRenderer.prepareTessellator(atlas);
//            Direction face;
//            ImmutableList<Quad> quads;
//            for (int vertexSet = 0, vertexSetCount = DIRECTIONS.length + 1; vertexSet < vertexSetCount; vertexSet++) {
//                face = vertexSet < 6 ? DIRECTIONS[vertexSet] : null;
//                random.setSeed(42);
//                quads = model.getQuads(null, null, face, random);
//                for (int i = 0, vertexesSize = quads.size(); i < vertexesSize; i++)
//                    quads.get(i).applyToVertexes(renderVertexNormal);
//            }
//            t.draw();
//        }
    }

    // TODO: add some kind of renderInventory() but for items
}
