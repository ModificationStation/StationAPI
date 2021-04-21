package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.Minecraft;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Shadow
    private Minecraft minecraft;

    @ModifyConstant(method = "method_1838(F)V", constant = @Constant(doubleValue = 3))
    private double getEntityReach(double originalReach) {
        return StationAPI.EVENT_BUS.post(new PlayerEvent.Reach(minecraft.player, HitType.ENTITY, originalReach)).currentReach;
    }
}
