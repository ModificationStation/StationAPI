package net.modificationstation.stationloader.mixin.client;

import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RemoteClientInteractionManager;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.item.CustomReach;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RemoteClientInteractionManager.class)
public abstract class MixinRemoteClientInteractionManager extends ClientInteractionManager {

    public MixinRemoteClientInteractionManager(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(method = "method_1715()F", at = @At("RETURN"), cancellable = true)
    private void onReach(CallbackInfoReturnable<Float> cir) {
        ItemInstance itemInstance = minecraft.player.getHeldItem();
        if (itemInstance == null) {
            if (CustomReach.getHandBlockReach() == null) {
                if (CustomReach.getDefaultBlockReach() != null)
                    cir.setReturnValue(CustomReach.getDefaultBlockReach());
            } else
                cir.setReturnValue(CustomReach.getHandBlockReach());
        } else {
            ItemBase itemBase = itemInstance.getType();
            if (itemBase instanceof CustomReach)
                cir.setReturnValue(((CustomReach) itemBase).getCustomBlockReach(itemInstance, cir.getReturnValue()));
            else if (CustomReach.getDefaultBlockReach() != null)
                cir.setReturnValue(CustomReach.getDefaultBlockReach());
        }
    }
}
