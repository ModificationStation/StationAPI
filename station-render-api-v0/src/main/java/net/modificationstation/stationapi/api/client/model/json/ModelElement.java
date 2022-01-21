package net.modificationstation.stationapi.api.client.model.json;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Axis;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;

@Environment(EnvType.CLIENT)
public class ModelElement {
   public final Vec3f from;
   public final Vec3f to;
   public final Map<Direction, ModelElementFace> faces;
   public final ModelRotation rotation;
   public final boolean shade;

   public ModelElement(Vec3f from, Vec3f to, Map<Direction, ModelElementFace> faces, @Nullable ModelRotation rotation, boolean shade) {
      this.from = from;
      this.to = to;
      this.faces = faces;
      this.rotation = rotation;
      this.shade = shade;
      this.initTextures();
   }

   private void initTextures() {

      for (Entry<Direction, ModelElementFace> directionModelElementFaceEntry : this.faces.entrySet()) {
         float[] fs = this.getRotatedMatrix(directionModelElementFaceEntry.getKey());
         directionModelElementFaceEntry.getValue().textureData.setUvs(fs);
      }

   }

   private float[] getRotatedMatrix(Direction direction) {
      switch(direction) {
      case DOWN:
         return new float[]{(float) this.from.x, (float) (16.0F - this.to.z), (float) this.to.x, (float) (16.0F - this.from.z)};
      case UP:
         return new float[]{(float) this.from.x, (float) this.from.z, (float) this.to.x, (float) this.to.z};
      case NORTH:
      default:
         return new float[]{(float) (16.0F - this.to.x), (float) (16.0F - this.to.y), (float) (16.0F - this.from.x), (float) (16.0F - this.from.y)};
      case SOUTH:
         return new float[]{(float) this.from.x, (float) (16.0F - this.to.y), (float) this.to.x, (float) (16.0F - this.from.y)};
      case WEST:
         return new float[]{(float) this.from.z, (float) (16.0F - this.to.y), (float) this.to.z, (float) (16.0F - this.from.y)};
      case EAST:
         return new float[]{(float) (16.0F - this.to.z), (float) (16.0F - this.to.y), (float) (16.0F - this.from.z), (float) (16.0F - this.from.y)};
      }
   }

   @Environment(EnvType.CLIENT)
   public static class Deserializer implements JsonDeserializer<ModelElement> {
      protected Deserializer() {
      }

      public ModelElement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         Vec3f vec3f = this.deserializeFrom(jsonObject);
         Vec3f vec3f1 = this.deserializeTo(jsonObject);
         ModelRotation modelRotation = this.deserializeRotation(jsonObject);
         Map<Direction, ModelElementFace> map = this.deserializeFacesValidating(jsonDeserializationContext, jsonObject);
         if (jsonObject.has("shade") && !JsonHelper.hasBoolean(jsonObject, "shade")) {
            throw new JsonParseException("Expected shade to be a Boolean");
         } else {
            boolean bl = JsonHelper.getBoolean(jsonObject, "shade", true);
            return new ModelElement(vec3f, vec3f1, map, modelRotation, bl);
         }
      }

      @Nullable
      private ModelRotation deserializeRotation(JsonObject object) {
         ModelRotation modelRotation = null;
         if (object.has("rotation")) {
            JsonObject jsonObject = JsonHelper.getObject(object, "rotation");
            Vec3f vec3f = this.deserializeVec3f(jsonObject, "origin");
            float scale = 0.0625F;
            vec3f.x *= scale;
            vec3f.y *= scale;
            vec3f.z *= scale;
            Axis axis = this.deserializeAxis(jsonObject);
            float f = this.deserializeRotationAngle(jsonObject);
            boolean bl = JsonHelper.getBoolean(jsonObject, "rescale", false);
            modelRotation = new ModelRotation(vec3f, axis, f, bl);
         }

         return modelRotation;
      }

      private float deserializeRotationAngle(JsonObject object) {
         float f = JsonHelper.getFloat(object, "angle");
         if (f != 0.0F && MathHelper.abs(f) != 22.5F && MathHelper.abs(f) != 45.0F) {
            throw new JsonParseException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
         } else {
            return f;
         }
      }

      private Axis deserializeAxis(JsonObject object) {
         String string = JsonHelper.getString(object, "axis");
         Axis axis = Axis.fromName(string.toLowerCase(Locale.ROOT));
         if (axis == null) {
            throw new JsonParseException("Invalid rotation axis: " + string);
         } else {
            return axis;
         }
      }

      private Map<Direction, ModelElementFace> deserializeFacesValidating(JsonDeserializationContext context, JsonObject object) {
         Map<Direction, ModelElementFace> map = this.deserializeFaces(context, object);
         if (map.isEmpty()) {
            throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
         } else {
            return map;
         }
      }

      private Map<Direction, ModelElementFace> deserializeFaces(JsonDeserializationContext context, JsonObject object) {
         Map<Direction, ModelElementFace> map = Maps.newEnumMap(Direction.class);
         JsonObject jsonObject = JsonHelper.getObject(object, "faces");

         for (Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
            Direction direction = this.getDirection(stringJsonElementEntry.getKey());
            map.put(direction, context.deserialize(stringJsonElementEntry.getValue(), ModelElementFace.class));
         }

         return map;
      }

      private Direction getDirection(String name) {
         Direction direction = Direction.byName(name);
         if (direction == null) {
            throw new JsonParseException("Unknown facing: " + name);
         } else {
            return direction;
         }
      }

      private Vec3f deserializeTo(JsonObject object) {
         Vec3f Vec3f = this.deserializeVec3f(object, "to");
         if (Vec3f.x >= -16.0F && Vec3f.y >= -16.0F && Vec3f.z >= -16.0F && Vec3f.x <= 32.0F && Vec3f.y <= 32.0F && Vec3f.z <= 32.0F) {
            return Vec3f;
         } else {
            throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + Vec3f);
         }
      }

      private Vec3f deserializeFrom(JsonObject object) {
         Vec3f Vec3f = this.deserializeVec3f(object, "from");
         if (Vec3f.x >= -16.0F && Vec3f.y >= -16.0F && Vec3f.z >= -16.0F && Vec3f.x <= 32.0F && Vec3f.y <= 32.0F && Vec3f.z <= 32.0F) {
            return Vec3f;
         } else {
            throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + Vec3f);
         }
      }

      private Vec3f deserializeVec3f(JsonObject object, String name) {
         JsonArray jsonArray = JsonHelper.getArray(object, name);
         if (jsonArray.size() != 3) {
            throw new JsonParseException("Expected 3 " + name + " values, found: " + jsonArray.size());
         } else {
            float[] fs = new float[3];

            for(int i = 0; i < fs.length; ++i) {
               fs[i] = JsonHelper.asFloat(jsonArray.get(i), name + "[" + i + "]");
            }

            return Vec3f.method_1293(fs[0], fs[1], fs[2]);
         }
      }
   }
}
