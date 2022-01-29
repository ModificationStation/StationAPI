package net.modificationstation.stationapi.impl.client.resource;

import com.google.gson.JsonObject;

public interface ResourceMetadataReader<T> {
    String getKey();

    T fromJson(JsonObject var1);
}
