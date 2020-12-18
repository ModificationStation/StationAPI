package net.modificationstation.stationloader.mixin.common;

import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.common.event.level.LevelInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class MixinLevel {

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void afterLevelInitialization(CallbackInfo ci) {
        LevelInit.EVENT.getInvoker().onLevelInit((Level) (Object) this);
    }
}
