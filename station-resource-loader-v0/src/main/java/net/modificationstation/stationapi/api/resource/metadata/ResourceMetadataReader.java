package net.modificationstation.stationapi.api.resource.metadata;

import com.google.gson.JsonObject;

public interface ResourceMetadataReader<T> {

    String getKey();

    T fromJson(JsonObject object);
}
