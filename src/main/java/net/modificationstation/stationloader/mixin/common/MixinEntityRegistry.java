package net.modificationstation.stationloader.mixin.common;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityRegistry;
import net.modificationstation.stationloader.api.common.event.entity.EntityRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRegistry.class)
public class MixinEntityRegistry {

    @Shadow
    private static void register(Class<? extends EntityBase> arg, String string, int i) {}

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onEntityRegister(CallbackInfo ci) {
        EntityRegister.EVENT.getInvoker().registerEntities(MixinEntityRegistry::register);
    }
}
