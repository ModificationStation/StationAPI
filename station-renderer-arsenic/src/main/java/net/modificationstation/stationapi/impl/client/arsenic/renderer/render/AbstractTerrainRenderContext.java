package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import com.google.common.primitives.Ints;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.client.render.material.ShadeMode;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.util.math.ColorHelper;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.MutableQuadViewImpl;

import java.nio.ByteOrder;
import java.util.Random;

public abstract class AbstractTerrainRenderContext extends AbstractRenderContext {
    protected final BlockRenderInfo blockInfo = new BlockRenderInfo();
    protected final BlockInputContextImpl inputContext = new BlockInputContextImpl();
    private final LightingCalculatorImpl aoCalc = new LightingCalculatorImpl(3);

    private int cachedTintIndex = -1;
    private int cachedTint;

    protected abstract VertexConsumer getVertexConsumer(int layer);

    /** Must be called before buffering a block model. */
    protected void prepare(BlockPos pos, BlockState state) {
        blockInfo.prepareForBlock(pos, state);
//        aoCalc.clear();
        cachedTintIndex = -1;
    }

    private float redI2F(int color) {
        return ((color >> 16) & 255) / 255F;
    }

    private float greenI2F(int color) {
        return ((color >> 8) & 255) / 255F;
    }

    private float blueI2F(int color) {
        return (color & 255) / 255F;
    }

    private int colorF2I(float r, float g, float b) {
        final int ri = colorChannelF2I(r), gi = colorChannelF2I(g), bi = colorChannelF2I(b);
        return ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ?
                (0xFF << 24) | (bi << 16) | (gi << 8) | ri :
                (ri << 24) | (gi << 16) | (bi << 8) | 0xFF;
    }

    private int colorChannelF2I(float colorChannel) {
        return Ints.constrainToRange((int) (colorChannel * 255), 0, 255);
    }

    @Override
    protected void bufferQuad(MutableQuadViewImpl quad) {
        if (blockInfo.shouldCullSide(quad.cullFace())) {
            return;
        }

        final RenderMaterial mat = quad.material();
        final boolean ao = blockInfo.effectiveAo(mat.ambientOcclusion());
        final boolean emissive = mat.emissive();
        final boolean vanillaShade = mat.shadeMode() == ShadeMode.VANILLA;
        final VertexConsumer vertexConsumer = getVertexConsumer(blockInfo.effectiveRenderLayer(mat.renderLayer()));

        tintQuad(quad);
        aoCalc.initialize(blockInfo.blockState.getBlock(), blockInfo.blockView, blockInfo.blockPos.x, blockInfo.blockPos.y, blockInfo.blockPos.z, ao);
        shadeQuad(quad, ao, emissive, vanillaShade);
        bufferQuad(quad, vertexConsumer);
    }

    private void tintQuad(MutableQuadViewImpl quad) {
        int tintIndex = quad.tintIndex();

        if (tintIndex != -1) {
            final int tint;

            if (tintIndex == cachedTintIndex) {
                tint = cachedTint;
            } else {
                cachedTint = tint = blockInfo.blockColor(tintIndex);
                cachedTintIndex = tintIndex;
            }

            for (int i = 0; i < 4; i++) {
                quad.color(i, ColorHelper.Argb.mixColor(quad.color(i), tint));
            }
        }
    }

    private void shadeQuad(MutableQuadViewImpl quad, boolean ao, boolean emissive, boolean vanillaShade) {
        aoCalc.calculateForQuad(quad);

        int color = quad.tintIndex();
        float[] brightness = aoCalc.light;
        
        float
                r = redI2F(color),
                g = greenI2F(color),
                b = blueI2F(color);

        for (int i = 0; i < 4; i++) {
            quad.color(i, colorF2I(r * brightness[0], g * brightness[0], b * brightness[0]));
        }
    }

    public class BlockInputContextImpl implements BakedModel.BlockInputContext {

        private boolean fluidModel;
        private BlockState blockState;
        private BlockPos pos;
        private BlockView blockView;
        private MatrixStack matrixStack;
        private Random random;

        public void prepare(boolean fluidModel, BlockState blockState, BlockPos pos, BlockView blockView, MatrixStack matrixStack, Random random) {
            this.fluidModel = fluidModel;
            this.blockState = blockState;
            this.pos = pos;
            this.blockView = blockView;
            this.matrixStack = matrixStack;
            this.random = random;
        }

        public void release() {
            this.fluidModel = false;
            this.blockState = null;
            this.pos = null;
            this.blockView = null;
            this.matrixStack = null;
            this.random = null;
        }

        @Override
        public boolean isFluidModel() {
            return this.fluidModel;
        }

        @Override
        public BlockState blockState() {
            return this.blockState;
        }

        @Override
        public BlockPos pos() {
            return this.pos;
        }

        @Override
        public BlockView blockView() {
            return this.blockView;
        }

        @Override
        public MatrixStack matrixStack() {
            return this.matrixStack;
        }

        @Override
        public Random random() {
            return this.random;
        }
    }
}
