package net.modificationstation.stationapi.api.common.event.recipe;

import net.modificationstation.stationapi.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface BeforeRecipeStats {

    GameEvent<BeforeRecipeStats> EVENT = new GameEvent<>(BeforeRecipeStats.class,
            listeners ->
                    () -> {
                        for (BeforeRecipeStats listener : listeners)
                            listener.beforeRecipeStats();
                    },
            (Consumer<GameEvent<BeforeRecipeStats>>) recipeRegister ->
                    recipeRegister.register(() -> GameEvent.EVENT_BUS.post(new Data()))
    );

    void beforeRecipeStats();

    final class Data extends GameEvent.Data<BeforeRecipeStats> {

        private Data() {
            super(EVENT);
        }
    }
}
