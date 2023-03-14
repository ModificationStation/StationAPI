package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.render.model.ModelRotation;
import net.modificationstation.stationapi.api.util.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
        this.faces.forEach((direction, value) -> {
            ModelElementTexture texture = value.textureData;
            if (texture.uvs == null) texture.uvs = this.getRotatedMatrix(direction);
        });
    }

    private float[] getRotatedMatrix(Direction direction) {
        return switch (direction) {
            case DOWN, UP -> new float[] { from.getX(), from.getZ(), to.getX(), to.getZ() }; // mirroring is handled in ModelElementTexture.getU()
            case EAST -> new float[] { 16 - to.getZ(), 16 - to.getY(), 16 - from.getZ(), 16 - from.getY() };
            case WEST -> new float[] { from.getZ(), 16 - to.getY(), to.getZ(), 16 - from.getY() };
            case NORTH -> new float[] { 16 - to.getX(), 16 - to.getY(), 16 - from.getX(), 16 - from.getY() };
            case SOUTH -> new float[] { from.getX(), 16 - to.getY(), to.getX(), 16 - from.getY() };
        };
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<ModelElement> {
        protected Deserializer() {
        }

        public ModelElement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Vec3f vec3f = this.deserializeFrom(jsonObject);
            Vec3f vec3f1 = this.deserializeTo(jsonObject);
            float tmp = vec3f.getZ();
            vec3f.set(vec3f.getX(), vec3f.getY(), 16 - vec3f1.getZ()); // modern has z inverted
            vec3f1.set(vec3f1.getX(), vec3f1.getY(), 16 - tmp);
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
                Vec3f vec3f = this.deserializeVec3f(jsonObject, "origin");
                vec3f.set(vec3f.getX(), vec3f.getY(), 16 - vec3f.getZ()); // modern has z inverted
                vec3f.scale(0.0625F);
                Direction.Axis axis = this.deserializeAxis(jsonObject);
                float f = this.deserializeRotationAngle(jsonObject);
                switch (axis) { // modern has x and z swapped
                    case X -> axis = Direction.Axis.Z;
                    case Z -> axis = Direction.Axis.X;
                }
                if (axis == Direction.Axis.Z) // modern has z inverted
                    f = -f;
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

            // modern changed cardinal directions, so the vertical axis faces have to be rotated appropriately
            map.entrySet().stream().filter(e -> e.getKey().getAxis().isVertical() && e.getValue().textureData.uvs != null).map(e -> e.getValue().textureData).forEach(textureData -> textureData.rotation = (textureData.rotation + 270) % 360);
            ModelElementFace texture = map.get(Direction.DOWN); // beta has the downward faces mirrored
            if (texture != null && texture.textureData.uvs != null) {
                texture.textureData.uvs = new float[] {
                        texture.textureData.uvs[2],
                        texture.textureData.uvs[1],
                        texture.textureData.uvs[0],
                        texture.textureData.uvs[3],
                };
            }

            return map;
        }

        private Direction getDirection(String name) {
            Direction direction = Direction.byName(name);
            if (direction == null) throw new JsonParseException("Unknown facing: " + name);
            else return direction;
        }

        private Vec3f deserializeTo(JsonObject object) {
            Vec3f Vec3f = this.deserializeVec3f(object, "to");
            if (Vec3f.getX() >= -16.0F && Vec3f.getY() >= -16.0F && Vec3f.getZ() >= -16.0F && Vec3f.getX() <= 32.0F && Vec3f.getY() <= 32.0F && Vec3f.getZ() <= 32.0F)
                return Vec3f;
            else throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + Vec3f);
        }

        private Vec3f deserializeFrom(JsonObject object) {
            Vec3f Vec3f = this.deserializeVec3f(object, "from");
            if (Vec3f.getX() >= -16.0F && Vec3f.getY() >= -16.0F && Vec3f.getZ() >= -16.0F && Vec3f.getX() <= 32.0F && Vec3f.getY() <= 32.0F && Vec3f.getZ() <= 32.0F)
                return Vec3f;
            else throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + Vec3f);
        }

        private Vec3f deserializeVec3f(JsonObject object, String name) {
            JsonArray jsonArray = JsonHelper.getArray(object, name);
            if (jsonArray.size() != 3)
                throw new JsonParseException("Expected 3 " + name + " values, found: " + jsonArray.size());
            else {
                float[] fs = new float[3];

                for(int i = 0; i < fs.length; ++i)
                    fs[i] = JsonHelper.asFloat(jsonArray.get(fs.length - i - 1), name + "[" + i + "]"); // modern has x and z swapped

                return new Vec3f(fs[0], fs[1], fs[2]);
            }
        }
    }
}
