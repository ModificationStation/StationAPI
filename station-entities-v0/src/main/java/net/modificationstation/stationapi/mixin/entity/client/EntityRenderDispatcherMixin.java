package net.modificationstation.stationapi.mixin.entity.client;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
class EntityRenderDispatcherMixin {
    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 27
            )
    )
    private <K, V> V stationapi_afterVanillaRender(Map<K, V> map, K key, V value) {
        V ret = map.put(key, value);
        //noinspection unchecked
        StationAPI.EVENT_BUS.post(EntityRendererRegisterEvent.builder().renderers((Map<Class<? extends Entity>, EntityRenderer>) map).build());
        return ret;
    }
}
