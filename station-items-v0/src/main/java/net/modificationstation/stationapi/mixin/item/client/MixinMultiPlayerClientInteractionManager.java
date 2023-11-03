package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.MultiplayerInteractionManager;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerInteractionManager.class)
public class MixinMultiPlayerClientInteractionManager extends InteractionManager {
    public MixinMultiPlayerClientInteractionManager(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(method = "method_1721", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"),cancellable = true)
    public void method_1721_preMine(int x, int y, int z, int side, CallbackInfo ci){
        ItemStack itemInstance = this.minecraft.player.inventory.getSelectedItem();
        if(itemInstance != null){
            if(!itemInstance.preMine(this.minecraft.player.world.getBlockState(x, y, z), x, y, z, side, this.minecraft.player)){
                ci.cancel();
            }
        }
    }
}
