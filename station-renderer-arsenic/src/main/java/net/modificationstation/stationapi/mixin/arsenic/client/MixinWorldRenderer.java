package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow private World level;
    @Shadow public float field_1803;

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;renderWithTexture(Lnet/minecraft/block/BlockBase;IIII)V"
            )
    )
    private void renderDamage(BlockRenderManager instance, Block block, int j, int k, int l, int texture, PlayerEntity arg, HitResult arg2, int i, ItemStack arg3, float f) {
        BlockState state = level.getBlockState(j, k, l);
        if (StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state) instanceof VanillaBakedModel)
            instance.renderWithTexture(block, j, k, l, texture);
        else
            Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).bakedModelRenderer().renderDamage(state, new BlockPos(j, k, l), level, field_1803);
    }
}
