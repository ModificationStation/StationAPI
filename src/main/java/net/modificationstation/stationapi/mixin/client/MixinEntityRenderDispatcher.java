package net.modificationstation.stationapi.mixin.client;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @SuppressWarnings("unchecked")
    @Redirect(method = "<init>()V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 27))
    private <K, V> V afterVanillaRender(Map<K, V> map, K key, V value) {
        V ret = map.put(key, value);
        EntityRendererRegister.EVENT.getInvoker().registerEntityRenderers((Map<Class<? extends EntityBase>, EntityRenderer>) map);
        return ret;
    }
}
