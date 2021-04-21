package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.class_608;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
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
        cir.setReturnValue((float) StationAPI.EVENT_BUS.post(new PlayerEvent.Reach(minecraft.player, HitType.TILE, cir.getReturnValueF())).currentReach);
    }
}
