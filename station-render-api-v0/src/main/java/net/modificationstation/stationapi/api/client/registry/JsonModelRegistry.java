package net.modificationstation.stationapi.api.client.registry;

import net.modificationstation.stationapi.api.client.model.JsonModel;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import org.jetbrains.annotations.NotNull;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public final class JsonModelRegistry extends Registry<JsonModel> {

    public static final JsonModelRegistry INSTANCE = new JsonModelRegistry(of(MODID, "json_models"));

    /**
     * Default registry constructor.
     *
     * @param identifier registry's identifier.
     */
    private JsonModelRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }
}
