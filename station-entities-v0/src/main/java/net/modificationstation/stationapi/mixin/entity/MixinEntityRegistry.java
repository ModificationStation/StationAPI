package net.modificationstation.stationapi.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityRegistry.class)
public class MixinEntityRegistry {

    @Shadow
    private static void register(Class<? extends Entity> arg, String string, int i) { }

    @Shadow private static Map<String, Class<? extends Entity>> STRING_ID_TO_CLASS;

    @Shadow private static Map<Class<? extends Entity>, String> CLASS_TO_STRING_ID;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onEntityRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                EntityRegister.builder()
                        .register(MixinEntityRegistry::register)
                        .registerNoID((aClass, s) -> {
                            STRING_ID_TO_CLASS.put(s, aClass);
                            CLASS_TO_STRING_ID.put(aClass, s);
                        })
                        .build()
        );
    }
}
