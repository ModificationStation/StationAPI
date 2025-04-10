package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import net.modificationstation.stationapi.api.world.BlockStateView;
import net.modificationstation.stationapi.mixin.arsenic.client.BlockRenderManagerAccessor;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

public final class ArsenicBlockRenderer {


    public static final int
            TEX_SIZE = 16,
            ATLAS_SIZE = 256;
    public static final double TEX_SIZE_OFFSET = 0.01;
    public static final float TEX_SIZE_OFFSET_F = (float) TEX_SIZE_OFFSET;
    public static final float ADJUSTED_TEX_SIZE = TEX_SIZE - TEX_SIZE_OFFSET_F;

    private static double extractMultiplier(double constant) {
        return constant / TEX_SIZE;
    }

    public static double adjustToWidth(double constant, Sprite sprite) {
        return extractMultiplier(constant) * sprite.getContents().getWidth();
    }

    public static double adjustToHeight(double constant, Sprite sprite) {
        return extractMultiplier(constant) * sprite.getContents().getHeight();
    }

    private static double extractTexCoordMultiplier(double constant) {
        return constant * ATLAS_SIZE / TEX_SIZE;
    }

    public static double adjustU(double constant, Sprite sprite) {
        return extractTexCoordMultiplier(constant) * sprite.getContents().getWidth() / StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    public static double adjustV(double constant, Sprite sprite) {
        return extractTexCoordMultiplier(constant) * sprite.getContents().getHeight() / StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    private static float extractMultiplier(float constant) {
        return constant / TEX_SIZE;
    }

    public static float adjustToWidth(float constant, Sprite sprite) {
        return extractMultiplier(constant) * sprite.getContents().getWidth();
    }

    public static float adjustToHeight(float constant, Sprite sprite) {
        return extractMultiplier(constant) * sprite.getContents().getHeight();
    }

    private static float extractTexCoordMultiplier(float constant) {
        return constant * ATLAS_SIZE / TEX_SIZE;
    }

    public static float adjustU(float constant, Sprite sprite) {
        return extractTexCoordMultiplier(constant) * sprite.getContents().getWidth() / StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    public static float adjustV(float constant, Sprite sprite) {
        return extractTexCoordMultiplier(constant) * sprite.getContents().getHeight() / StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    private static int extractMultiplier(int constant) {
        return constant / TEX_SIZE;
    }

    public static int adjustToWidth(int constant, Sprite sprite) {
        return extractMultiplier(constant * sprite.getContents().getWidth());
    }

    public static int adjustToHeight(int constant, Sprite sprite) {
        return extractMultiplier(constant * sprite.getContents().getHeight());
    }

    private final BlockRenderManager blockRenderer;
    private final BlockRenderManagerAccessor blockRendererAccessor;
    private final Random random = new Random();
    private final MutableBlockPos blockPos = new MutableBlockPos(0, 0, 0);

    public ArsenicBlockRenderer(BlockRenderManager blockRenderer) {
        this.blockRenderer = blockRenderer;
        blockRendererAccessor = (BlockRenderManagerAccessor) blockRenderer;
    }

    public void renderWorld(VertexConsumer consumer, Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = ((BlockStateView) blockRendererAccessor.getBlockView()).getBlockState(x, y, z);
        BakedModel model = StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state);
        if (!model.isBuiltin()) {
            cir.setReturnValue(RendererHolder.RENDERER.renderBlock(consumer, state, blockPos.set(x, y, z), blockRendererAccessor.getBlockView(), !blockRendererAccessor.getSkipFaceCulling(), random));
        } else //noinspection deprecation
            if (block instanceof BlockWithWorldRenderer renderer) {
                block.updateBoundingBox(blockRendererAccessor.getBlockView(), x, y, z);
                //noinspection deprecation
                cir.setReturnValue(renderer.renderWorld(consumer, blockRenderer, blockRendererAccessor.getBlockView(), x, y, z));
        }
    }

    public void renderInventory(VertexConsumer consumer, Block block, int meta, float brightness, CallbackInfo ci) {
        if (block instanceof BlockWithInventoryRenderer renderer) {
            if (blockRenderer.inventoryColorEnabled) {
                int var5 = block.getColor(meta);
                float var6 = (float)((var5 >> 16) & 255) / 255.0F;
                float var7 = (float)((var5 >> 8) & 255) / 255.0F;
                float var8 = (float)(var5 & 255) / 255.0F;
                GL11.glColor4f(var6 * brightness, var7 * brightness, var8 * brightness, 1.0F);
            }
            renderer.renderInventory(consumer, blockRenderer, meta);
            ci.cancel();
        }
    }
}
