package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.util.math.ColorHelper;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.MutableQuadViewImpl;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.IntFunction;

public class SimpleBlockRenderContext extends AbstractRenderContext {
    public static final ThreadLocal<SimpleBlockRenderContext> POOL = ThreadLocal.withInitial(SimpleBlockRenderContext::new);

    private final Random random = new Random();

    private IntFunction<VertexConsumer> vertexConsumers;
    private int defaultRenderLayer;
    private float red;
    private float green;
    private float blue;
    private int light;

    @Nullable
    private int lastRenderLayer;
    @Nullable
    private VertexConsumer lastVertexConsumer;

    @Override
    protected void bufferQuad(MutableQuadViewImpl quad) {
        final RenderMaterial mat = quad.material();
        final int renderLayer = mat.renderLayer();
        final VertexConsumer vertexConsumer;

        if (renderLayer == lastRenderLayer) {
            vertexConsumer = lastVertexConsumer;
        } else {
            lastVertexConsumer = vertexConsumer = vertexConsumers.apply(renderLayer);
            lastRenderLayer = renderLayer;
        }

        tintQuad(quad);
//        shadeQuad(quad, mat.emissive());
        bufferQuad(quad, vertexConsumer);
    }

    private void tintQuad(MutableQuadViewImpl quad) {
        if (quad.tintIndex() != -1) {
            final float red = this.red;
            final float green = this.green;
            final float blue = this.blue;

            for (int i = 0; i < 4; i++) {
                quad.color(i, ColorHelper.Argb.scaleRgb(quad.color(i), red, green, blue));
            }
        }
    }

    public void bufferModel(MatrixStack entry, IntFunction<VertexConsumer> vertexConsumers, BakedModel model, float red, float green, float blue, int light, BlockView blockView, BlockPos pos, BlockState state) {
        matrices = entry.peek();

        this.vertexConsumers = vertexConsumers;
        this.defaultRenderLayer = state.getBlock().getRenderLayer();
        this.red = MathHelper.clamp(red, 0, 1);
        this.green = MathHelper.clamp(green, 0, 1);
        this.blue = MathHelper.clamp(blue, 0, 1);
        this.light = light;

        random.setSeed(42L);

        model.emitBlockQuads(new BakedModel.BlockInputContext() {
            @Override
            public boolean isFluidModel() {
                return false;
            }

            @Override
            public BlockState blockState() {
                return state;
            }

            @Override
            public BlockPos pos() {
                return pos;
            }

            @Override
            public BlockView blockView() {
                return blockView;
            }

            @Override
            public MatrixStack matrixStack() {
                return entry;
            }

            @Override
            public Random random() {
                return random;
            }
        }, getEmitter());

        matrices = null;
        this.vertexConsumers = null;
        lastRenderLayer = 1;
        lastVertexConsumer = null;
    }
}
