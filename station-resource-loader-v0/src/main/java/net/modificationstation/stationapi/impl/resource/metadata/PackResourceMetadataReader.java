package net.modificationstation.stationapi.impl.resource.metadata;

import com.google.gson.JsonObject;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataSerializer;

public class PackResourceMetadataReader implements ResourceMetadataSerializer<PackResourceMetadata> {
    @Override
    public PackResourceMetadata fromJson(JsonObject jsonObject) {
//        MutableText text = Text.Serializer.fromJson(jsonObject.get("description"));
//        if (text == null) {
//            throw new JsonParseException("Invalid/missing description!");
//        }
//        int i = JsonHelper.getInt(jsonObject, "pack_format");
//        return new PackResourceMetadata(text, i);
        return new PackResourceMetadata("fixText", 13);
    }

    @Override
    public JsonObject toJson(PackResourceMetadata packResourceMetadata) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("description", packResourceMetadata.getDescription());
        jsonObject.addProperty("pack_format", packResourceMetadata.getPackFormat());
        return jsonObject;
    }

    @Override
    public String getKey() {
        return "pack";
    }
}

