package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

@Environment(EnvType.CLIENT)
public class ModelElementFace {
    public final Direction cullFace;
    public final int tintIndex;
    public final String textureId;
    public final ModelElementTexture textureData;
    public final float emission;

    public ModelElementFace(@Nullable Direction cullFace, int tintIndex, String textureId, ModelElementTexture textureData) {
        this(cullFace, tintIndex, textureId, textureData, 0);
    }

    public ModelElementFace(@Nullable Direction cullFace, int tintIndex, String textureId, ModelElementTexture textureData, float emission) {
        this.cullFace = cullFace;
        this.tintIndex = tintIndex;
        this.textureId = textureId;
        this.textureData = textureData;
        this.emission = emission;
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<ModelElementFace> {
        protected Deserializer() {
        }

        public ModelElementFace deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Direction direction = this.deserializeCullFace(jsonObject);
            if (direction != null) direction = direction.rotateClockwise(Direction.Axis.Y);
            int i = this.deserializeTintIndex(jsonObject);
            String string = this.deserializeTexture(jsonObject);
            ModelElementTexture modelElementTexture = jsonDeserializationContext.deserialize(jsonObject, ModelElementTexture.class);
            float emission = JsonHelper.getFloat(jsonObject, "emission", 0F);
            emission = MathHelper.clamp(emission, 0F, 1F);
            return new ModelElementFace(direction, i, string, modelElementTexture, emission);
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
