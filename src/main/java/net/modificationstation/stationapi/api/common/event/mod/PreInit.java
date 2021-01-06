package net.modificationstation.stationapi.api.common.event.mod;

import lombok.Getter;
import net.modificationstation.stationapi.api.common.event.EventRegistry;
import net.modificationstation.stationapi.api.common.event.ModEvent;
import net.modificationstation.stationapi.api.common.recipe.JsonRecipeParserRegistry;
import net.modificationstation.stationapi.api.common.registry.ModID;

/**
 * Event called before Minecraft launch
 * <p>
 * args: none
 * return: void
 *
 * @author mine_diver
 */

public interface PreInit {

    ModEvent<PreInit> EVENT = new ModEvent<>(PreInit.class,
            listeners ->
                    (eventRegistry, jsonRecipeParserRegistry, modID) -> {
                        for (PreInit listener : listeners)
                            listener.preInit(eventRegistry, jsonRecipeParserRegistry, PreInit.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (eventRegistry, jsonRecipeParserRegistry, modID) -> {
                        PreInit.EVENT.setCurrentListener(listener);
                        listener.preInit(eventRegistry, jsonRecipeParserRegistry, modID);
                        PreInit.EVENT.setCurrentListener(null);
                    },
            preInit ->
                    preInit.register((eventRegistry, jsonRecipeParserRegistry, modID) -> ModEvent.post(new Data(eventRegistry, jsonRecipeParserRegistry)), null)
    );

    void preInit(EventRegistry eventRegistry, JsonRecipeParserRegistry jsonRecipeParserRegistry, ModID modID);

    final class Data extends ModInitData<PreInit> {

        @Getter
        private final EventRegistry eventRegistry;
        @Getter
        private final JsonRecipeParserRegistry jsonRecipeParserRegistry;

        private Data(EventRegistry eventRegistry, JsonRecipeParserRegistry jsonRecipeParserRegistry) {
            super(EVENT);
            this.eventRegistry = eventRegistry;
            this.jsonRecipeParserRegistry = jsonRecipeParserRegistry;
        }
    }
}
