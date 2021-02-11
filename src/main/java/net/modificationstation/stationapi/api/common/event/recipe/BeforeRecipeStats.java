package net.modificationstation.stationapi.api.common.event.recipe;

import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface BeforeRecipeStats {

    GameEventOld<BeforeRecipeStats> EVENT = new GameEventOld<>(BeforeRecipeStats.class,
            listeners ->
                    () -> {
                        for (BeforeRecipeStats listener : listeners)
                            listener.beforeRecipeStats();
                    },
            (Consumer<GameEventOld<BeforeRecipeStats>>) recipeRegister ->
                    recipeRegister.register(() -> GameEventOld.EVENT_BUS.post(new Data()))
    );

    void beforeRecipeStats();

    final class Data extends GameEventOld.Data<BeforeRecipeStats> {

        private Data() {
            super(EVENT);
        }
    }
}
