package net.modificationstation.stationapi.mixin.item.server;

import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.UseOnBlockFirst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_70.class)
public class Mixinclass_70 {

    @Shadow public PlayerEntity field_2309;

    @Inject(method = "method_1832(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/level/Level;Lnet/minecraft/item/ItemInstance;IIII)Z", at = @At(value = "HEAD"), cancellable = true)
    private void injectOnPlaceBlock(PlayerEntity playerBase, World level, ItemStack itemInstance, int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir) {
        if (itemInstance != null && itemInstance.getItem() instanceof UseOnBlockFirst use && use.onUseOnBlockFirst(itemInstance, playerBase, level, i, j, k, i1))
            cir.setReturnValue(true);
    }

    @Inject(method = "method_1830", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getTileId(III)I"), cancellable = true)
    public void method_1830_preMine(int x, int y, int z, int side, CallbackInfo ci){
        ItemStack itemInstance = this.field_2309.inventory.getSelectedItem();
        if(itemInstance != null){
            if(!itemInstance.preMine(this.field_2309.world.getBlockState(x,y,z), x, y, z, side, this.field_2309)){
                ci.cancel();
            }
        }
    }
}
