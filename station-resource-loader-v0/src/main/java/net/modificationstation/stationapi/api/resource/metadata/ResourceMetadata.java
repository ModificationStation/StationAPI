package net.modificationstation.stationapi.api.resource.metadata;

import com.google.gson.JsonObject;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.util.JsonHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public interface ResourceMetadata {

    ResourceMetadata NONE = new ResourceMetadata() {

        @Override
        public <T> Optional<T> decode(ResourceMetadataReader<T> reader) {
            return Optional.empty();
        }
    };

    InputSupplier<ResourceMetadata> NONE_SUPPLIER = () -> NONE;

    static ResourceMetadata create(InputStream stream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))){
            final JsonObject jsonObject = JsonHelper.deserialize(bufferedReader);
            return new ResourceMetadata() {

                @Override
                public <T> Optional<T> decode(ResourceMetadataReader<T> reader) {
                    String key = reader.getKey();
                    return jsonObject.has(key) ? Optional.of(reader.fromJson(JsonHelper.getObject(jsonObject, key))) : Optional.empty();
                }
            };
        }
    }

    <T> Optional<T> decode(ResourceMetadataReader<T> reader);
}

