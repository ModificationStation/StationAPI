package net.modificationstation.stationapi.mixin.sortme.client;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.StationAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @SuppressWarnings({"unchecked", "UnresolvedMixinReference"}) // Fernflower bad, CFR good.
    @Redirect(method = "<init>()V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 27))
    private <K, V> V afterVanillaRender(Map<K, V> map, K key, V value) {
        V ret = map.put(key, value);
        StationAPI.EVENT_BUS.post(new EntityRendererRegisterEvent((Map<Class<? extends EntityBase>, EntityRenderer>) map));
        return ret;
    }
}
