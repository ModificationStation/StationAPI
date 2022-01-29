package net.modificationstation.stationapi.api.client.model.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;

@Environment(EnvType.CLIENT)
public class ModelElementTexture {
   public float[] uvs;
   public int rotation;

   public ModelElementTexture(@SuppressWarnings("NullableProblems") @Nullable float[] uvs, int rotation) {
      this.uvs = uvs;
      this.rotation = rotation;
   }

   public float getU(int rotation, Direction direction) {
//      if (this.uvs == null) {
//         throw new NullPointerException("uvs");
//      } else {
//         switch (direction) {
//            case DOWN:
//               switch (rotation) {
//                  case 0:
//                  case 1:
//                     return uvs[0];
//                  case 2:
//                  case 3:
//                     return uvs[2];
//                  default:
//                     throw new IllegalStateException("Unexpected value: " + rotation);
//               }
//            case UP:
//               switch (rotation) {
//
//               }
//            case EAST:
//               break;
//            case WEST:
//               break;
//            case NORTH:
//               break;
//            case SOUTH:
//               break;
//            default:
//               throw new IllegalStateException("Unexpected value: " + direction);
//         }
//      }
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int i = this.getRotatedUVIndex(rotation);
         switch (direction) {
            case DOWN:
            case UP:
            case WEST:
               return this.uvs[i == 0 || i == 1 ? 0 : 2];
            case EAST:
            case NORTH:
               return this.uvs[i == 0 || i == 3 ? 2 : 0];
            case SOUTH:
               return this.uvs[i == 0 || i == 3 ? 0 : 2];
            default:
               throw new IllegalStateException("Unexpected value: " + direction);
         }
      }
   }

   public float getV(int rotation, Direction direction) {
//      if (this.uvs == null) {
//         throw new NullPointerException("uvs");
//      } else {
//         switch (direction) {
//            case DOWN:
//               switch (rotation) {
//                  case 0:
//                     return uvs[3];
//                  case 1:
//                     return uvs[1];
//                  case 2:
//                     return uvs[1];
//                  case 3:
//                     return uvs[3];
//                  default:
//                     throw new IllegalStateException("Unexpected value: " + rotation);
//               }
//            case UP:
//               switch (rotation) {
//
//               }
//            case EAST:
//               break;
//            case WEST:
//               break;
//            case NORTH:
//               break;
//            case SOUTH:
//               break;
//            default:
//               throw new IllegalStateException("Unexpected value: " + direction);
//         }
//      }
      if (this.uvs == null) {
         throw new NullPointerException("uvs");
      } else {
         int i = this.getRotatedUVIndex(rotation);
         switch (direction) {
            case DOWN:
            case UP:
            case WEST:
               return this.uvs[i == 0 || i == 3 ? 1 : 3];
            case EAST:
            case NORTH:
               return this.uvs[i == 0 || i == 1 ? 1 : 3];
            case SOUTH:
               return this.uvs[i == 0 || i == 1 ? 3 : 1];
            default:
               throw new IllegalStateException("Unexpected value: " + direction);
         }
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
