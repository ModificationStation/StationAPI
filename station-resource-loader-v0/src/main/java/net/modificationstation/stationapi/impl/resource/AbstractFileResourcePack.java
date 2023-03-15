package net.modificationstation.stationapi.impl.resource;

import com.google.gson.JsonObject;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourcePack;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.api.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public abstract class AbstractFileResourcePack implements ResourcePack {
    private final String name;
    private final boolean alwaysStable;

    protected AbstractFileResourcePack(String name, boolean alwaysStable) {
        this.name = name;
        this.alwaysStable = alwaysStable;
    }

    @Override
    @Nullable
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
        InputSupplier<InputStream> inputSupplier = this.openRoot("pack.mcmeta");
        if (inputSupplier == null) return null;
        try (InputStream inputStream = inputSupplier.get()){
            return AbstractFileResourcePack.parseMetadata(metaReader, inputStream);
        }
    }

    @Nullable
    public static <T> T parseMetadata(ResourceMetadataReader<T> metaReader, InputStream inputStream) {
        JsonObject jsonObject;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
            jsonObject = JsonHelper.deserialize(bufferedReader);
        } catch (Exception exception) {
            LOGGER.error("Couldn't load {} metadata", metaReader.getKey(), exception);
            return null;
        }
        if (!jsonObject.has(metaReader.getKey())) return null;
        try {
            return metaReader.fromJson(JsonHelper.getObject(jsonObject, metaReader.getKey()));
        } catch (Exception exception) {
            LOGGER.error("Couldn't load {} metadata", metaReader.getKey(), exception);
            return null;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isAlwaysStable() {
        return this.alwaysStable;
    }
}

