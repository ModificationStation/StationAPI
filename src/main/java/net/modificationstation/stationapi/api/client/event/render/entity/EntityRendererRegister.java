package net.modificationstation.stationapi.api.client.event.render.entity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.Map;
import java.util.function.Consumer;

public interface EntityRendererRegister {

    GameEventOld<EntityRendererRegister> EVENT = new GameEventOld<>(EntityRendererRegister.class,
            listeners ->
                    renderers -> {
                        for (EntityRendererRegister listener : listeners)
                            listener.registerEntityRenderers(renderers);
                    },
            (Consumer<GameEventOld<EntityRendererRegister>>) entityRendererRegister ->
                    entityRendererRegister.register(renderers -> GameEventOld.EVENT_BUS.post(new Data(renderers)))
    );

    void registerEntityRenderers(Map<Class<? extends EntityBase>, EntityRenderer> renderers);

    final class Data extends GameEventOld.Data<EntityRendererRegister> {

        public final Map<Class<? extends EntityBase>, EntityRenderer> renderers;

        private Data(Map<Class<? extends EntityBase>, EntityRenderer> renderers) {
            super(EVENT);
            this.renderers = renderers;
        }
    }
}
