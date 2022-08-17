package net.modificationstation.stationapi.api.resource;

import com.google.gson.JsonObject;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.api.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class AbstractFileResourcePack {

    public static String getFilename(ResourceType type, Identifier id) {
        return String.format("%s/%s/%s", type.getDirectory(), id.modID, id.id);
    }

    @Nullable
    public static <T> T parseMetadata(ResourceMetadataReader<T> metaReader, InputStream inputStream) {
        JsonObject jsonObject;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));){
            jsonObject = JsonHelper.deserialize(bufferedReader);
        }
        catch (Exception exception) {
            LOGGER.error("Couldn't load {} metadata", metaReader.getKey(), exception);
            return null;
        }
        if (!jsonObject.has(metaReader.getKey())) {
            return null;
        }
        try {
            return metaReader.fromJson(JsonHelper.getObject(jsonObject, metaReader.getKey()));
        }
        catch (Exception exception) {
            LOGGER.error("Couldn't load {} metadata", metaReader.getKey(), exception);
            return null;
        }
    }
}
