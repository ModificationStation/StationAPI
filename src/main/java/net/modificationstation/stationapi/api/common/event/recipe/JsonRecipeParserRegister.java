package net.modificationstation.stationapi.api.common.event.recipe;

import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.recipe.JsonRecipeParserRegistry;

public class JsonRecipeParserRegister extends RegistryEvent<JsonRecipeParserRegistry> {

    public JsonRecipeParserRegister() {
        super(JsonRecipeParserRegistry.INSTANCE);
    }
}
