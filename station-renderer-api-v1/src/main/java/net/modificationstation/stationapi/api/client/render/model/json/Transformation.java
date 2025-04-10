package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.JsonHelper;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Type;

@Environment(EnvType.CLIENT)
public class Transformation {
    public static final Transformation IDENTITY = new Transformation(new Vector3f(), new Vector3f(), new Vector3f(1.0F, 1.0F, 1.0F));
    public final Vector3f rotation;
    public final Vector3f translation;
    public final Vector3f scale;

    public Transformation(Vector3f rotation, Vector3f translation, Vector3f scale) {
        this.rotation = new Vector3f(rotation);
        this.translation = new Vector3f(translation);
        this.scale = new Vector3f(scale);
    }

    public void apply() {
        if (this != IDENTITY) {
            float f = this.rotation.x();
            float g = this.rotation.y();
            float h = this.rotation.z();

            GL11.glTranslatef(this.translation.x(), this.translation.y(), this.translation.z());
            GL11.glRotatef(f, 1, 0, 0);
            GL11.glRotatef(g, 0, 1, 0);
            GL11.glRotatef(h, 0, 0, 1);
            GL11.glScalef(this.scale.x(), this.scale.y(), this.scale.z());
        }
    }

    public void apply(MatrixStack matrices) {
        if (this != IDENTITY) {
            float f = this.rotation.x();
            float g = this.rotation.y();
            float h = this.rotation.z();

            matrices.translate(this.translation.x(), this.translation.y(), this.translation.z());
            matrices.multiply(new Quaternionf().rotationXYZ(f * (float) (Math.PI / 180.0), g * (float) (Math.PI / 180.0), h * (float) (Math.PI / 180.0)));
            matrices.scale(this.scale.x(), this.scale.y(), this.scale.z());
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
        private static final Vector3f DEFAULT_ROTATION = new Vector3f(0.0F, 0.0F, 0.0F);
        private static final Vector3f DEFAULT_TRANSLATION = new Vector3f(0.0F, 0.0F, 0.0F);
        private static final Vector3f DEFAULT_SCALE = new Vector3f(1.0F, 1.0F, 1.0F);

        protected Deserializer() {
        }

        public Transformation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Vector3f vector3f = this.parseVector3f(jsonObject, "rotation", DEFAULT_ROTATION);
            Vector3f vector3f2 = this.parseVector3f(jsonObject, "translation", DEFAULT_TRANSLATION);
            vector3f2.mul(0.0625F);
            vector3f2.set(MathHelper.clamp(vector3f2.x, -5.0F, 5.0F), MathHelper.clamp(vector3f2.y, -5.0F, 5.0F), MathHelper.clamp(vector3f2.z, -5.0F, 5.0F));
            Vector3f vector3f3 = this.parseVector3f(jsonObject, "scale", DEFAULT_SCALE);
            vector3f3.set(MathHelper.clamp(vector3f3.x, -4.0F, 4.0F), MathHelper.clamp(vector3f3.y, -4.0F, 4.0F), MathHelper.clamp(vector3f3.z, -4.0F, 4.0F));
            return new Transformation(vector3f, vector3f2, vector3f3);
        }

        private Vector3f parseVector3f(JsonObject json, String key, Vector3f fallback) {
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

                    return new Vector3f(fs[0], fs[1], fs[2]);
                }
            }
        }
    }
}
