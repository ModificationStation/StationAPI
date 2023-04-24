package net.modificationstation.stationapi.api.resource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.JsonHelper;
import net.modificationstation.stationapi.api.util.profiler.Profiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * An abstract implementation of resource reloader that reads JSON files
 * into Gson representations in the prepare stage.
 */
public abstract class JsonDataLoader extends SinglePreparationResourceReloader<Map<Identifier, JsonElement>> {
    private final Gson gson;
    private final String dataType;

    public JsonDataLoader(Gson gson, String dataType) {
        this.gson = gson;
        this.dataType = dataType;
    }

    @Override
    protected Map<Identifier, JsonElement> prepare(ResourceManager resourceManager, Profiler profiler) {
        Reference2ReferenceMap<Identifier, JsonElement> result = new Reference2ReferenceOpenHashMap<>();
        ResourceFinder jsonFinder = ResourceFinder.json(this.dataType);
        for (Map.Entry<Identifier, Resource> found : jsonFinder.findResources(resourceManager).entrySet()) {
            Identifier path = found.getKey();
            Identifier id = jsonFinder.toResourceId(path);
            try (BufferedReader reader = found.getValue().getReader()) {
                JsonElement element = JsonHelper.deserialize(this.gson, reader, JsonElement.class);
                JsonElement previousElement = result.put(id, element);
                if (previousElement == null) continue;
                throw new IllegalStateException("Duplicate data file ignored with ID " + id);
            } catch (JsonParseException | IOException | IllegalArgumentException exception) {
                LOGGER.error("Couldn't parse data file {} from {}", id, path, exception);
            }
        }
        return result;
    }
}

