package net.modificationstation.stationloader.api.client.event.render.entity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.Map;

public interface EntityRendererRegister {

    SimpleEvent<EntityRendererRegister> EVENT = new SimpleEvent<>(EntityRendererRegister.class, (listeners) ->
            (renderers) -> {
                for (EntityRendererRegister event : listeners)
                    event.registerEntityRenderers(renderers);
            });

    void registerEntityRenderers(Map<Class<? extends EntityBase>, EntityRenderer> renderers);
}
