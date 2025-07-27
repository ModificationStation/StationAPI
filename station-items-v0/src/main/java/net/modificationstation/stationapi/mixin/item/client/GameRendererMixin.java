package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.hit.HitResultType;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameRenderer.class)
class GameRendererMixin {
    @Shadow
    private Minecraft field_2349;

    @ModifyConstant(
            method = "method_1838(F)V",
            constant = @Constant(doubleValue = 3)
    )
    private double stationapi_getEntityReach(double originalReach) {
        return StationAPI.EVENT_BUS.post(
                PlayerEvent.Reach.builder()
                        .player(field_2349.player)
                        .type(HitResultType.ENTITY)
                        .currentReach(originalReach)
                        .build()
        ).currentReach;
    }
}
