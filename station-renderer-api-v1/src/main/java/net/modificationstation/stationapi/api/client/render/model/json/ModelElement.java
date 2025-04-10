package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.client.render.model.ModelRotation;
import net.modificationstation.stationapi.api.util.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

@Environment(EnvType.CLIENT)
public class ModelElement {
    public final Vector3f from;
    public final Vector3f to;
    public final Map<Direction, ModelElementFace> faces;
    public final ModelRotation rotation;
    public final boolean shade;

    public ModelElement(Vector3f from, Vector3f to, Map<Direction, ModelElementFace> faces, @Nullable ModelRotation rotation, boolean shade) {
        this.from = from;
        this.to = to;
        this.faces = faces;
        this.rotation = rotation;
        this.shade = shade;
        this.initTextures();
    }

    private void initTextures() {
        this.faces.forEach((direction, value) -> {
            ModelElementTexture texture = value.textureData;
            if (texture.uvs == null) texture.uvs = this.getRotatedMatrix(direction);
        });
    }

    private float[] getRotatedMatrix(Direction direction) {
        return switch (direction) {
            case DOWN -> new float[] { from.x(), 16 - to.z(), to.x(), 16 - from.z() };
            case UP -> new float[] { from.x(), from.z(), to.x(), to.z() };
            case NORTH -> new float[] { 16 - to.x(), 16 - to.y(), 16 - from.x(), 16 - from.y() };
            case SOUTH -> new float[] { from.x(), 16 - to.y(), to.x(), 16 - from.y() };
            case WEST -> new float[] { from.z(), 16 - to.y(), to.z(), 16 - from.y() };
            case EAST -> new float[] { 16 - to.z(), 16 - to.y(), 16 - from.z(), 16 - from.y() };
        };
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<ModelElement> {
        protected Deserializer() {
        }

        public ModelElement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Vector3f vec3f = this.deserializeFrom(jsonObject);
            Vector3f vec3f1 = this.deserializeTo(jsonObject);
            ModelRotation modelRotation = this.deserializeRotation(jsonObject);
            Map<Direction, ModelElementFace> map = this.deserializeFacesValidating(jsonDeserializationContext, jsonObject);
            if (jsonObject.has("shade") && !JsonHelper.hasBoolean(jsonObject, "shade"))
                throw new JsonParseException("Expected shade to be a Boolean");
            else {
                boolean bl = JsonHelper.getBoolean(jsonObject, "shade", true);
                return new ModelElement(vec3f, vec3f1, map, modelRotation, bl);
            }
        }

        @Nullable
        private ModelRotation deserializeRotation(JsonObject object) {
            ModelRotation modelRotation = null;
            if (object.has("rotation")) {
                JsonObject jsonObject = JsonHelper.getObject(object, "rotation");
                Vector3f vec3f = this.deserializeVec3f(jsonObject, "origin");
                vec3f.mul(0.0625F);
                Direction.Axis axis = this.deserializeAxis(jsonObject);
                float f = this.deserializeRotationAngle(jsonObject);
                boolean bl = JsonHelper.getBoolean(jsonObject, "rescale", false);
                modelRotation = new ModelRotation(vec3f, axis, f, bl);
            }

            return modelRotation;
        }

        private float deserializeRotationAngle(JsonObject object) {
            float f = JsonHelper.getFloat(object, "angle");
            if (f != 0.0F && MathHelper.abs(f) != 22.5F && MathHelper.abs(f) != 45.0F)
                throw new JsonParseException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
            else return f;
        }

        private Direction.Axis deserializeAxis(JsonObject object) {
            String string = JsonHelper.getString(object, "axis");
            Direction.Axis axis = Direction.Axis.fromName(string.toLowerCase(Locale.ROOT));
            if (axis == null) throw new JsonParseException("Invalid rotation axis: " + string);
            else return axis;
        }

        private Map<Direction, ModelElementFace> deserializeFacesValidating(JsonDeserializationContext context, JsonObject object) {
            Map<Direction, ModelElementFace> map = this.deserializeFaces(context, object);
            if (map.isEmpty()) throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
            else return map;
        }

        private Map<Direction, ModelElementFace> deserializeFaces(JsonDeserializationContext context, JsonObject object) {
            Map<Direction, ModelElementFace> map = new EnumMap<>(Direction.class);
            JsonObject jsonObject = JsonHelper.getObject(object, "faces");

            for (Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
                Direction direction = this.getDirection(stringJsonElementEntry.getKey());
                map.put(direction, context.deserialize(stringJsonElementEntry.getValue(), ModelElementFace.class));
            }

            return map;
        }

        private Direction getDirection(String name) {
            Direction direction = Direction.byId(name).rotateClockwise(Direction.Axis.Y);
            if (direction == null) throw new JsonParseException("Unknown facing: " + name);
            else return direction;
        }

        private Vector3f deserializeTo(JsonObject object) {
            Vector3f Vec3f = this.deserializeVec3f(object, "to");
            if (Vec3f.x() >= -16.0F && Vec3f.y() >= -16.0F && Vec3f.z() >= -16.0F && Vec3f.x() <= 32.0F && Vec3f.y() <= 32.0F && Vec3f.z() <= 32.0F)
                return Vec3f;
            else throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + Vec3f);
        }

        private Vector3f deserializeFrom(JsonObject object) {
            Vector3f Vec3f = this.deserializeVec3f(object, "from");
            if (Vec3f.x() >= -16.0F && Vec3f.y() >= -16.0F && Vec3f.z() >= -16.0F && Vec3f.x() <= 32.0F && Vec3f.y() <= 32.0F && Vec3f.z() <= 32.0F)
                return Vec3f;
            else throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + Vec3f);
        }

        private Vector3f deserializeVec3f(JsonObject object, String name) {
            JsonArray jsonArray = JsonHelper.getArray(object, name);
            if (jsonArray.size() != 3)
                throw new JsonParseException("Expected 3 " + name + " values, found: " + jsonArray.size());
            else {
                float[] fs = new float[3];

                for(int i = 0; i < fs.length; ++i)
                    fs[i] = JsonHelper.asFloat(jsonArray.get(i), name + "[" + i + "]"); // modern has x and z swapped

                return new Vector3f(fs[0], fs[1], fs[2]);
            }
        }
    }
}
