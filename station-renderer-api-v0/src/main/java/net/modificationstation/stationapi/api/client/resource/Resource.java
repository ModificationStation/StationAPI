package net.modificationstation.stationapi.api.client.resource;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import net.modificationstation.stationapi.impl.client.resource.ResourceMetadataReader;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public interface Resource extends Closeable {

    InputStream getInputStream();

    default Optional<InputStream> getMeta() {
        return Optional.empty();
    }

    default String getResourcePackName() {
        //noinspection deprecation
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack.name;
    }

    @Nullable
    default <T> T getMetadata(ResourceMetadataReader<T> reader) {
        Optional<InputStream> meta = getMeta();
        if (meta.isEmpty())
            return null;
        BufferedReader bufferedReader = null;
        JsonObject mcmeta;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(meta.get(), StandardCharsets.UTF_8));
            mcmeta = JsonHelper.deserialize(bufferedReader);
        }
        catch (Throwable throwable) {
            IOUtils.closeQuietly(bufferedReader);
            throw throwable;
        }
        IOUtils.closeQuietly(bufferedReader);
        String key = reader.getKey();
        return mcmeta.has(key) ? reader.fromJson(JsonHelper.getObject(mcmeta, key)) : null;
    }

    @Override
    default void close() throws IOException {
        getInputStream().close();
        Optional<InputStream> meta = getMeta();
        if (meta.isPresent())
            meta.get().close();
    }

    static Resource of(InputStream stream) {
        return stream instanceof Resource resource ? resource : () -> stream;
    }
}
