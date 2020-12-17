package net.modificationstation.stationloader.api.client.event.render.entity;

import lombok.Getter;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.Map;
import java.util.function.Consumer;

public interface EntityRendererRegister {

    @SuppressWarnings("UnstableApiUsage")
    GameEvent<EntityRendererRegister> EVENT = new GameEvent<>(EntityRendererRegister.class,
            listeners ->
                    renderers -> {
                        for (EntityRendererRegister listener : listeners)
                            listener.registerEntityRenderers(renderers);
                    },
            (Consumer<GameEvent<EntityRendererRegister>>) entityRendererRegister ->
                    entityRendererRegister.register(renderers -> GameEvent.EVENT_BUS.post(new Data(renderers)))
    );

    void registerEntityRenderers(Map<Class<? extends EntityBase>, EntityRenderer> renderers);

    final class Data extends GameEvent.Data<EntityRendererRegister> {

        @Getter
        private final Map<Class<? extends EntityBase>, EntityRenderer> renderers;

        private Data(Map<Class<? extends EntityBase>, EntityRenderer> renderers) {
            super(EVENT);
            this.renderers = renderers;
        }
    }
}
