package net.modificationstation.stationloader.mixin.client;

import net.minecraft.class_608;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.item.CustomReach;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_608.class)
public class Mixinclass_608 extends ClientInteractionManager {

    public Mixinclass_608(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(method = "getBlockReachDistance()F", at = @At("RETURN"), cancellable = true)
    private void getBlockReach(CallbackInfoReturnable<Float> cir) {
        Float defaultBlockReach = CustomReach.getDefaultBlockReach();
        Float handBlockReach = CustomReach.getHandBlockReach();
        if (defaultBlockReach != null)
            cir.setReturnValue(defaultBlockReach);
        ItemInstance itemInstance = minecraft.player.getHeldItem();
        if (itemInstance == null) {
            if(handBlockReach != null)
                cir.setReturnValue(handBlockReach);
        } else {
            ItemBase itemBase = itemInstance.getType();
            if (itemBase instanceof CustomReach)
                cir.setReturnValue(((CustomReach) itemBase).getCustomBlockReach(itemInstance, cir.getReturnValue()));
        }
    }
}
