package net.modificationstation.stationapi.mixin.item.server;

import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.item.UseOnBlockFirst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_70.class)
public class Mixinclass_70 {

    @Shadow public PlayerBase field_2309;

    @Inject(method = "method_1832(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/level/Level;Lnet/minecraft/item/ItemInstance;IIII)Z", at = @At(value = "HEAD"), cancellable = true)
    private void injectOnPlaceBlock(PlayerBase playerBase, Level level, ItemInstance itemInstance, int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir) {
        if (itemInstance != null && itemInstance.getType() instanceof UseOnBlockFirst use && use.onUseOnBlockFirst(itemInstance, playerBase, level, i, j, k, i1))
            cir.setReturnValue(true);
    }

    @Inject(method = "method_1830", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getTileId(III)I"), cancellable = true)
    public void method_1830_preMine(int x, int y, int z, int par4, CallbackInfo ci){
        ItemInstance itemInstance = this.field_2309.inventory.getHeldItem();
        if(itemInstance != null){
            if(!itemInstance.preMine(x,y,z,par4,this.field_2309)){
                ci.cancel();
            }
        }
    }
}
