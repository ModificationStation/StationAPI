package net.modificationstation.stationapi.api.common.event.mod;

import net.modificationstation.stationapi.api.common.event.EventRegistry;
import net.modificationstation.stationapi.api.common.event.ModEventOld;
import net.modificationstation.stationapi.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationapi.api.common.recipe.JsonRecipeParserRegistry;
import net.modificationstation.stationapi.api.common.registry.ModID;

/**
 * PreInitialization event called for mods to do some set up involving adding new StAPI events, JSON recipe parsers, etc...
 * Some additional setup can be done as well, but Minecraft classes can not be referenced during this event.
 * @author mine_diver
 * @see EventRegistry
 * @see JsonRecipeParserRegistry
 */
public interface PreInit {

    /**
     * The event instance.
     */
    ModEventOld<PreInit> EVENT = new ModEventOld<>(PreInit.class,
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
                    preInit.register((eventRegistry, jsonRecipeParserRegistry, modID) -> ModEventOld.post(new Data(eventRegistry, jsonRecipeParserRegistry)), null)
    );

    /**
     * The event function.
     * @param eventRegistry the event registry used to initialize event listeners through fabric.mod.json entrypoints.
     * @param jsonRecipeParserRegistry the JSON recipe parser registry that holds all JSON recipe parsers to automatically run when {@link RecipeRegister} event is called with a proper identifier.
     * @param modID current listener's ModID.
     */
    void preInit(EventRegistry eventRegistry, JsonRecipeParserRegistry jsonRecipeParserRegistry, ModID modID);

    /**
     * The event data used by EventBus.
     */
    final class Data extends ModInitData<PreInit> {

        /**
         * The event registry used to initialize event listeners through fabric.mod.json entrypoints.
         */
        public final EventRegistry eventRegistry;

        /**
         * The JSON recipe parser registry that holds all JSON recipe parsers to automatically run when {@link RecipeRegister} event is called with a proper identifier.
         */
        public final JsonRecipeParserRegistry jsonRecipeParserRegistry;

        private Data(EventRegistry eventRegistry, JsonRecipeParserRegistry jsonRecipeParserRegistry) {
            super(EVENT);
            this.eventRegistry = eventRegistry;
            this.jsonRecipeParserRegistry = jsonRecipeParserRegistry;
        }
    }
}
