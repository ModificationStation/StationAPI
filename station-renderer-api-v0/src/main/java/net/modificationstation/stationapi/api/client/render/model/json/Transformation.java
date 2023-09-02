package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Type;

@Environment(EnvType.CLIENT)
public class Transformation {
    public static final Transformation IDENTITY = new Transformation(new Vec3f(), new Vec3f(), new Vec3f(1.0F, 1.0F, 1.0F));
    public final Vec3f rotation;
    public final Vec3f translation;
    public final Vec3f scale;

    public Transformation(Vec3f rotation, Vec3f translation, Vec3f scale) {
        this.rotation = rotation.copy();
        this.translation = translation.copy();
        this.scale = scale.copy();
    }

    public void apply() {
        if (this != IDENTITY) {
            float f = this.rotation.getX();
            float g = this.rotation.getY();
            float h = this.rotation.getZ();

            GL11.glTranslatef(this.translation.getX(), this.translation.getY(), this.translation.getZ());
            GL11.glRotatef(f, 1, 0, 0);
            GL11.glRotatef(g, 0, 1, 0);
            GL11.glRotatef(h, 0, 0, 1);
            GL11.glScalef(this.scale.getX(), this.scale.getY(), this.scale.getZ());
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (this.getClass() != o.getClass()) {
            return false;
        } else {
            Transformation transformation = (Transformation)o;
            return this.rotation.equals(transformation.rotation) && this.scale.equals(transformation.scale) && this.translation.equals(transformation.translation);
        }
    }

    public int hashCode() {
        int i = this.rotation.hashCode();
        i = 31 * i + this.translation.hashCode();
        i = 31 * i + this.scale.hashCode();
        return i;
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<Transformation> {
        private static final Vec3f DEFAULT_ROTATION = new Vec3f(0.0F, 0.0F, 0.0F);
        private static final Vec3f DEFAULT_TRANSLATION = new Vec3f(0.0F, 0.0F, 0.0F);
        private static final Vec3f DEFAULT_SCALE = new Vec3f(1.0F, 1.0F, 1.0F);

        protected Deserializer() {
        }

        public Transformation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Vec3f vector3f = this.parseVector3f(jsonObject, "rotation", DEFAULT_ROTATION);
            Vec3f vector3f2 = this.parseVector3f(jsonObject, "translation", DEFAULT_TRANSLATION);
            vector3f2.set(vector3f2.getZ(), vector3f2.getY(), -vector3f2.getX()); // modern has x and z swapped and z inverted
            vector3f2.scale(0.0625F);
            vector3f2.clamp(-5.0F, 5.0F);
            Vec3f vector3f3 = this.parseVector3f(jsonObject, "scale", DEFAULT_SCALE);
            vector3f3.clamp(-4.0F, 4.0F);
            return new Transformation(vector3f, vector3f2, vector3f3);
        }

        private Vec3f parseVector3f(JsonObject json, String key, Vec3f fallback) {
            if (!json.has(key)) {
                return fallback;
            } else {
                JsonArray jsonArray = JsonHelper.getArray(json, key);
                if (jsonArray.size() != 3) {
                    throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonArray.size());
                } else {
                    float[] fs = new float[3];

                    for(int i = 0; i < fs.length; ++i) {
                        fs[i] = JsonHelper.asFloat(jsonArray.get(i), key + "[" + i + "]");
                    }

                    return new Vec3f(fs[0], fs[1], fs[2]);
                }
            }
        }
    }
}
