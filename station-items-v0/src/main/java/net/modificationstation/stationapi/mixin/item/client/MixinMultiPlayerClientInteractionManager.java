package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiPlayerClientInteractionManager;
import net.minecraft.item.ItemInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerClientInteractionManager.class)
public class MixinMultiPlayerClientInteractionManager extends BaseClientInteractionManager {
    public MixinMultiPlayerClientInteractionManager(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(method = "method_1721", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"),cancellable = true)
    public void method_1721_preMine(int x, int y, int z, int par4, CallbackInfo ci){
        ItemInstance itemInstance = this.minecraft.player.inventory.getHeldItem();
        if(itemInstance != null){
            if(!itemInstance.preMine(x,y,z,par4,this.minecraft.player)){
                ci.cancel();
            }
        }
    }
}
