package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.gl.VertexBuffer;
import net.modificationstation.stationapi.api.client.render.BufferBuilderStorage;
import net.modificationstation.stationapi.api.client.render.OverlayVertexConsumer;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.api.world.BlockStateView;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow private Level level;

    private static final BufferBuilderStorage bufferBuilders = new BufferBuilderStorage();
    @Unique
    private final MatrixStack damageMtrx = new MatrixStack();

    private boolean stationapi$isVanilla;

    @Inject(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V"
            )
    )
    private void checkModel(PlayerBase arg2, HitResult i, int arg3, ItemInstance f, float par5, CallbackInfo ci) {
        stationapi$isVanilla = StationRenderAPI.getBakedModelManager().getBlockModels().getModel(((StationFlatteningWorld) level).getBlockState(i.x, i.y, i.z)) instanceof VanillaBakedModel; // TODO: maybe implement a better check? looks good enough, but still
    }

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V"
            )
    )
    private void begoneTessellator1(Tessellator instance) {
        if (stationapi$isVanilla)
            instance.start();
    }

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;setOffset(DDD)V"
            )
    )
    private void begoneTessellator2(Tessellator instance, double e, double f, double v) {
        if (stationapi$isVanilla)
            instance.setOffset(e, f, v);
    }

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;disableColour()V"
            )
    )
    private void begoneTessellator3(Tessellator instance) {
        if (stationapi$isVanilla)
            instance.disableColour();
    }

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;draw()V"
            )
    )
    private void begoneTessellator4(Tessellator instance) {
        if (stationapi$isVanilla)
            instance.draw();
    }

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;renderWithTexture(Lnet/minecraft/block/BlockBase;IIII)V"
            )
    )
    private void renderDamage(BlockRenderer instance, BlockBase block, int j, int k, int l, int texture, PlayerBase arg, HitResult arg2, int i, ItemInstance arg3, float f) {
        if (stationapi$isVanilla)
            instance.renderWithTexture(block, j, k, l, texture);
        else {
            damageMtrx.push();
            double d = arg.prevRenderX + (arg.x - arg.prevRenderX) * (double) f;
            double d2 = arg.prevRenderY + (arg.y - arg.prevRenderY) * (double) f;
            double d3 = arg.prevRenderZ + (arg.z - arg.prevRenderZ) * (double) f;
            damageMtrx.translate(-d, -d2, -d3);
            MatrixStack.Entry entry = damageMtrx.peek();
            OverlayVertexConsumer vertexConsumer = new OverlayVertexConsumer(bufferBuilders.getEffectVertexConsumers().getBuffer(ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.get(texture - 240)), entry.getPositionMatrix(), entry.getNormalMatrix());
            BlockState state = ((BlockStateView) level).getBlockState(j, k, l);
            Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).bakedModelRenderer().renderDamage(state, new TilePos(j, k, l), level, damageMtrx, vertexConsumer);
            damageMtrx.pop();
            bufferBuilders.getEffectVertexConsumers().draw();
//        VertexBuffer.unbindVertexArray();
            VertexBuffer.unbind();
            GL11.glEnable(2929);
        }
    }
}
