package net.modificationstation.stationapi.api.client.registry;

import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import org.jetbrains.annotations.NotNull;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public final class ModelRegistry extends Registry<Model> {

    public static final ModelRegistry INSTANCE = new ModelRegistry(of(MODID, "models"));

    /**
     * Default registry constructor.
     *
     * @param identifier registry's identifier.
     */
    private ModelRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }
}
