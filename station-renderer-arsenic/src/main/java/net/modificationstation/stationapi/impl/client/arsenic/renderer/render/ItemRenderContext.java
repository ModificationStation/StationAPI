package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.client.render.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.MutableQuadViewImpl;

import java.util.Random;

public class ItemRenderContext extends AbstractRenderContext {
    public static final ThreadLocal<ItemRenderContext> POOL = ThreadLocal.withInitial(ItemRenderContext::new);
    private VertexConsumer vertexConsumer;

    @Override
    protected void bufferQuad(MutableQuadViewImpl quad) {
        bufferQuad(quad, vertexConsumer);
    }

    public void renderItem(VertexConsumer vertexConsumer, ItemStack item, BakedModel model, Random random, float brightness) {
        renderItem(new MatrixStack(), vertexConsumer, item, model, random, brightness);
    }

    public void renderItem(MatrixStack matrixStack, VertexConsumer vertexConsumer, ItemStack item, BakedModel model, Random random, float brightness) {
        this.vertexConsumer = vertexConsumer;
        this.matrices = matrixStack.peek();
        model.emitItemQuads(new BakedModel.ItemInputContext() {
            @Override
            public ItemStack itemStack() {
                return item;
            }

            @Override
            public BlockView blockView() {
                return null;
            }

            @Override
            public MatrixStack matrixStack() {
                return matrixStack;
            }

            @Override
            public Random random() {
                return random;
            }
        }, getEmitter());
    }

    private void tintQuad(MutableQuadViewImpl quad) {
//        int tintIndex = quad.tintIndex();
//
//        if (tintIndex >= 0 && tintIndex < tints.length) {
//            final int tint = tints[tintIndex];
//
//            for (int i = 0; i < 4; i++) {
//                quad.color(i, net.minecraft.util.math.ColorHelper.mix(quad.color(i), tint));
//            }
//        }
    }

    private void shadeQuad(MutableQuadViewImpl quad, boolean emissive) {
//        if (emissive) {
//            for (int i = 0; i < 4; i++) {
//                quad.lightmap(i, LightmapTextureManager.MAX_LIGHT_COORDINATE);
//            }
//        } else {
//            final int light = this.light;
//
//            for (int i = 0; i < 4; i++) {
//                quad.lightmap(i, ColorHelper.maxLight(quad.lightmap(i), light));
//            }
//        }
    }
}
