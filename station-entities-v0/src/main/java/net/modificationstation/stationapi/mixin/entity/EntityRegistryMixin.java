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
class EntityRegistryMixin {
    @Shadow
    private static void register(Class<? extends Entity> arg, String string, int i) { }

    @Shadow private static Map<String, Class<? extends Entity>> idToClass;

    @Shadow private static Map<Class<? extends Entity>, String> classToId;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void stationapi_onEntityRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                EntityRegister.builder()
                        .register(EntityRegistryMixin::register)
                        .registerNoID((aClass, s) -> {
                            idToClass.put(s, aClass);
                            classToId.put(aClass, s);
                        })
                        .build()
        );
    }
}
