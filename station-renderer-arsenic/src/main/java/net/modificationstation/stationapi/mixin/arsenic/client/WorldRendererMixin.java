package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(WorldRenderer.class)
class WorldRendererMixin {
    @Shadow private World world;
    @Shadow public float field_1803;

    @Redirect(
            method = "method_1547",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderWithTexture(Lnet/minecraft/block/Block;IIII)V"
            )
    )
    private void stationapi_renderDamage(BlockRenderManager instance, Block block, int j, int k, int l, int texture, PlayerEntity arg, HitResult arg2, int i, ItemStack arg3, float f) {
        BlockState state = world.getBlockState(j, k, l);
        if (StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state) instanceof VanillaBakedModel)
            instance.renderWithTexture(block, j, k, l, texture);
        else
            Objects.requireNonNull(Renderer.get()).bakedModelRenderer().renderDamage(Tessellator.INSTANCE, state, new BlockPos(j, k, l), world, field_1803);
    }
}
