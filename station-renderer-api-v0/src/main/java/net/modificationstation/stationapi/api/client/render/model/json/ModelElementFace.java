package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

@Environment(EnvType.CLIENT)
public class ModelElementFace {
    public final Direction cullFace;
    public final int tintIndex;
    public final String textureId;
    public final ModelElementTexture textureData;

    public ModelElementFace(@Nullable Direction cullFace, int tintIndex, String textureId, ModelElementTexture textureData) {
        this.cullFace = cullFace;
        this.tintIndex = tintIndex;
        this.textureId = textureId;
        this.textureData = textureData;
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<ModelElementFace> {
        protected Deserializer() {
        }

        public ModelElementFace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Direction direction = this.deserializeCullFace(jsonObject);
            int i = this.deserializeTintIndex(jsonObject);
            String string = this.deserializeTexture(jsonObject);
            ModelElementTexture modelElementTexture = jsonDeserializationContext.deserialize(jsonObject, ModelElementTexture.class);
            return new ModelElementFace(direction, i, string, modelElementTexture);
        }

        protected int deserializeTintIndex(JsonObject object) {
            return JsonHelper.getInt(object, "tintindex", -1);
        }

        private String deserializeTexture(JsonObject object) {
            return JsonHelper.getString(object, "texture");
        }

        @Nullable
        private Direction deserializeCullFace(JsonObject object) {
            String string = JsonHelper.getString(object, "cullface", "");
            return Direction.byName(string);
        }
    }
}
