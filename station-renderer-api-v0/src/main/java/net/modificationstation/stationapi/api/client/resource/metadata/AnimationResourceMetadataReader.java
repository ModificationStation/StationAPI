package net.modificationstation.stationapi.api.client.resource.metadata;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.api.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

@Environment(value=EnvType.CLIENT)
public class AnimationResourceMetadataReader
implements ResourceMetadataReader<AnimationResourceMetadata> {
    @Override
    public AnimationResourceMetadata fromJson(JsonObject jsonObject) {
        List<AnimationFrameResourceMetadata> list = new ArrayList<>();
        int i = JsonHelper.getInt(jsonObject, "frametime", 1);
        if (i != 1) {
            Preconditions.checkArgument(i >= 1, "Invalid default frame time");
        }
        if (jsonObject.has("frames")) {
            try {
                JsonArray jsonArray = JsonHelper.getArray(jsonObject, "frames");
                for (int j = 0; j < jsonArray.size(); ++j) {
                    JsonElement jsonElement = jsonArray.get(j);
                    AnimationFrameResourceMetadata animationFrameResourceMetadata = this.readFrameMetadata(j, jsonElement);
                    if (animationFrameResourceMetadata == null) continue;
                    list.add(animationFrameResourceMetadata);
                }
            }
            catch (ClassCastException classCastException) {
                throw new JsonParseException("Invalid animation->frames: expected array, was " + jsonObject.get("frames"), classCastException);
            }
        }
        int k = JsonHelper.getInt(jsonObject, "width", -1);
        int l = JsonHelper.getInt(jsonObject, "height", -1);
        if (k != -1) {
            Preconditions.checkArgument(k >= 1, "Invalid width");
        }
        if (l != -1) {
            Preconditions.checkArgument(l >= 1, "Invalid height");
        }
        boolean bl = JsonHelper.getBoolean(jsonObject, "interpolate", false);
        return new AnimationResourceMetadata(list, k, l, i, bl);
    }

    private AnimationFrameResourceMetadata readFrameMetadata(int frame, JsonElement json) {
        if (json.isJsonPrimitive()) {
            return new AnimationFrameResourceMetadata(JsonHelper.asInt(json, "frames[" + frame + "]"));
        }
        if (json.isJsonObject()) {
            JsonObject jsonObject = JsonHelper.asObject(json, "frames[" + frame + "]");
            int i = JsonHelper.getInt(jsonObject, "time", -1);
            if (jsonObject.has("time")) {
                Preconditions.checkArgument(i >= 1, "Invalid frame time");
            }
            int j = JsonHelper.getInt(jsonObject, "index");
            Preconditions.checkArgument(j >= 0, "Invalid frame index");
            return new AnimationFrameResourceMetadata(j, i);
        }
        return null;
    }

    @Override
    public String getKey() {
        return "animation";
    }
}
