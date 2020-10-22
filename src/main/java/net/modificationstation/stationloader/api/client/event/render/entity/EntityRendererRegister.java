package net.modificationstation.stationloader.api.client.event.render.entity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

import java.util.Map;

public interface EntityRendererRegister {

    Event<EntityRendererRegister> EVENT = EventFactory.INSTANCE.newEvent(EntityRendererRegister.class, (listeners) ->
            (renderers) -> {
                for (EntityRendererRegister event : listeners)
                    event.registerEntityRenderers(renderers);
            });

    void registerEntityRenderers(Map<Class<? extends EntityBase>, EntityRenderer> renderers);
}
