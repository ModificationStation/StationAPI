package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

@Environment(EnvType.CLIENT)
public class ModelElementTexture {
   public float[] uvs;
   public int rotation;

   public ModelElementTexture(@SuppressWarnings("NullableProblems") @Nullable float[] uvs, int rotation) {
      this.uvs = uvs;
      this.rotation = rotation;
   }

   public float getU(int rotation, Direction direction) {
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int i = this.getRotatedUVIndex(rotation);
         return switch (direction) {
            case DOWN, UP, WEST -> this.uvs[i == 0 || i == 1 ? 0 : 2];
            case EAST, NORTH -> this.uvs[i == 0 || i == 3 ? 2 : 0];
            case SOUTH -> this.uvs[i == 0 || i == 3 ? 0 : 2];
         };
      }
   }

   public float getV(int rotation, Direction direction) {
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int i = this.getRotatedUVIndex(rotation);
         return switch (direction) {
            case DOWN, UP, WEST -> this.uvs[i == 0 || i == 3 ? 1 : 3];
            case EAST, NORTH -> this.uvs[i == 0 || i == 1 ? 1 : 3];
            case SOUTH -> this.uvs[i == 0 || i == 1 ? 3 : 1];
         };
      }
   }

   public float getU(int rotation) {
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int i = this.getRotatedUVIndex(rotation);
         return this.uvs[i != 0 && i != 1 ? 2 : 0];
      }
   }

   public float getV(int rotation) {
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int i = this.getRotatedUVIndex(rotation);
         return this.uvs[i != 0 && i != 3 ? 3 : 1];
      }
   }

   private int getRotatedUVIndex(int rotation) {
      return (rotation + this.rotation / 90) % 4;
   }

   public int getDirectionIndex(int offset) {
      return (offset + 4 - this.rotation / 90) % 4;
   }

   public void setUvs(float[] uvs) {
      if (this.uvs == null) {
         this.uvs = uvs;
      }

   }

   @Environment(EnvType.CLIENT)
   public static class Deserializer implements JsonDeserializer<ModelElementTexture> {
      protected Deserializer() {
      }

      public ModelElementTexture deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         float[] fs = this.deserializeUVs(jsonObject);
         int i = this.deserializeRotation(jsonObject);
         return new ModelElementTexture(fs, i);
      }

      protected int deserializeRotation(JsonObject object) {
         int i = JsonHelper.getInt(object, "rotation", 0);
         if (i >= 0 && i % 90 == 0 && i / 90 <= 3) {
            return i;
         } else {
            throw new JsonParseException("Invalid rotation " + i + " found, only 0/90/180/270 allowed");
         }
      }

      @SuppressWarnings("NullableProblems")
      @Nullable
      private float[] deserializeUVs(JsonObject object) {
         if (!object.has("uv")) {
            return null;
         } else {
            JsonArray jsonArray = JsonHelper.getArray(object, "uv");
            if (jsonArray.size() != 4) {
               throw new JsonParseException("Expected 4 uv values, found: " + jsonArray.size());
            } else {
               float[] fs = new float[4];

               for(int i = 0; i < fs.length; ++i) {
                  fs[i] = JsonHelper.asFloat(jsonArray.get(i), "uv[" + i + "]");
               }

               return fs;
            }
         }
      }
   }
}
