package net.modificationstation.stationapi.mixin.level;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class MixinLevel {

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void afterLevelInitialization(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new LevelEvent.Init((Level) (Object) this));
    }
}
