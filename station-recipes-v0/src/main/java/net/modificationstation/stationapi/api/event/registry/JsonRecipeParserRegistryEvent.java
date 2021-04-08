package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.registry.JsonRecipeParserRegistry;

public class JsonRecipeParserRegistryEvent extends RegistryEvent<JsonRecipeParserRegistry> {

    public JsonRecipeParserRegistryEvent() {
        super(JsonRecipeParserRegistry.INSTANCE);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
