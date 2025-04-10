package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.material.MaterialFinder;
import net.modificationstation.stationapi.api.client.render.mesh.MutableQuadView;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.mesh.QuadView;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElementFace;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElementTexture;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.*;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import java.lang.Math;

@Environment(EnvType.CLIENT)
public class BakedQuadFactory {
    private static final float MIN_SCALE = 1.0F / (float)Math.cos(0.39269909262657166D) - 1.0F;
    private static final float MAX_SCALE = 1.0F / (float)Math.cos(0.7853981852531433D) - 1.0F;

    public void emitQuad(QuadEmitter emitter, Vector3f from, Vector3f to, ModelElementFace face, Sprite texture, Direction side, ModelBakeSettings settings, @Nullable ModelRotation rotation, boolean shade, Identifier modelId) {
        ModelElementTexture modelElementTexture = face.textureData;
        if (settings.isUvLocked()) {
            modelElementTexture = uvLock(face.textureData, side, settings.getRotation(), modelId);
        }

        float[] fs = new float[modelElementTexture.uvs.length];
        System.arraycopy(modelElementTexture.uvs, 0, fs, 0, fs.length);
        float f = texture.getAnimationFrameDelta();
        float g = (modelElementTexture.uvs[0] + modelElementTexture.uvs[0] + modelElementTexture.uvs[2] + modelElementTexture.uvs[2]) / 4.0F;
        float h = (modelElementTexture.uvs[1] + modelElementTexture.uvs[1] + modelElementTexture.uvs[3] + modelElementTexture.uvs[3]) / 4.0F;
        modelElementTexture.uvs[0] = MathHelper.lerp(f, modelElementTexture.uvs[0], g);
        modelElementTexture.uvs[2] = MathHelper.lerp(f, modelElementTexture.uvs[2], g);
        modelElementTexture.uvs[1] = MathHelper.lerp(f, modelElementTexture.uvs[1], h);
        modelElementTexture.uvs[3] = MathHelper.lerp(f, modelElementTexture.uvs[3], h);
        this.packVertexData(emitter, modelElementTexture, texture, side, this.getPositionMatrix(from, to), settings.getRotation(), rotation, shade);

        Direction direction = decodeDirection(emitter);
        System.arraycopy(fs, 0, modelElementTexture.uvs, 0, fs.length);
        if (rotation == null) {
            emitter.nominalFace(direction);
        }

        emitter.tintIndex(face.tintIndex);

        MaterialFinder finder = Renderer.get().materialFinder();

        if (!shade) {
            finder.disableDiffuse(true);
        }

        if (face.cullFace != null)
            emitter.cullFace(Direction.transform(settings.getRotation().copyMatrix(), face.cullFace));

//        emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
        emitter.material(finder.find());
        emitter.tag(0);

        emitter.emit();
    }

    public static ModelElementTexture uvLock(ModelElementTexture texture, Direction orientation, AffineTransformation rotation, Identifier modelId) {
        Matrix4f matrix4f = AffineTransformations.uvLock(rotation, orientation, () -> "Unable to resolve UVLock for model: " + modelId).copyMatrix();
        float f = texture.getU(texture.getDirectionIndex(0));
        float g = texture.getV(texture.getDirectionIndex(0));
        Vector4f vector4f = matrix4f.transform(new Vector4f(f / 16.0F, g / 16.0F, 0.0F, 1.0F));
        float h = 16.0F * vector4f.x();
        float i = 16.0F * vector4f.y();
        float j = texture.getU(texture.getDirectionIndex(2));
        float k = texture.getV(texture.getDirectionIndex(2));
        Vector4f vector4f2 = matrix4f.transform(new Vector4f(j / 16.0F, k / 16.0F, 0.0F, 1.0F));
        float l = 16.0F * vector4f2.x();
        float m = 16.0F * vector4f2.y();
        float p;
        float q;
        if (Math.signum(j - f) == Math.signum(l - h)) {
            p = h;
            q = l;
        } else {
            p = l;
            q = h;
        }

        float t;
        float u;
        if (Math.signum(k - g) == Math.signum(m - i)) {
            t = i;
            u = m;
        } else {
            t = m;
            u = i;
        }

        float v = (float)Math.toRadians(texture.rotation);
        Matrix3f matrix3f = new Matrix3f(matrix4f);
        Vector3f vector3f = matrix3f.transform(new Vector3f(net.minecraft.util.math.MathHelper.cos(v), net.minecraft.util.math.MathHelper.sin(v), 0.0F));
        int w = Math.floorMod(-((int)Math.round(Math.toDegrees(Math.atan2(vector3f.y(), vector3f.x())) / 90.0D)) * 90, 360);
        return new ModelElementTexture(new float[]{p, t, q, u}, w);
    }

    private void packVertexData(QuadEmitter emitter, ModelElementTexture texture, Sprite sprite, Direction direction, float[] positionMatrix, AffineTransformation orientation, @Nullable ModelRotation rotation, boolean shaded) {
        for(int i = 0; i < 4; ++i) {
            this.packVertexData(emitter, i, direction, texture, positionMatrix, sprite, orientation, rotation, shaded);
        }
    }

    private float[] getPositionMatrix(Vector3f from, Vector3f to) {
        float[] fs = new float[Direction.values().length];
        fs[CubeFace.DirectionIds.EAST] = from.z() / 16.0F;
        fs[CubeFace.DirectionIds.DOWN] = from.y() / 16.0F;
        fs[CubeFace.DirectionIds.NORTH] = from.x() / 16.0F;
        fs[CubeFace.DirectionIds.WEST] = to.z() / 16.0F;
        fs[CubeFace.DirectionIds.UP] = to.y() / 16.0F;
        fs[CubeFace.DirectionIds.SOUTH] = to.x() / 16.0F;
        return fs;
    }

    private void packVertexData(QuadEmitter emitter, int cornerIndex, Direction direction, ModelElementTexture texture, float[] positionMatrix, Sprite sprite, AffineTransformation orientation, @Nullable ModelRotation rotation, boolean shaded) {
        CubeFace.Corner corner = CubeFace.getFace(direction).getCorner(cornerIndex);
        Vector3f vector3f = new Vector3f(positionMatrix[corner.xSide], positionMatrix[corner.ySide], positionMatrix[corner.zSide]);
        this.rotateVertex(vector3f, rotation);
        this.transformVertex(vector3f, orientation);
        this.packVertexData(emitter, cornerIndex, vector3f, sprite, texture);
    }

    private void packVertexData(QuadEmitter emitter, int vertexIndex, Vector3f position, Sprite sprite, ModelElementTexture modelElementTexture) {
        emitter.pos(vertexIndex, position);

        emitter.uv(vertexIndex, sprite.getFrameU(modelElementTexture.getU(vertexIndex)), sprite.getFrameV(modelElementTexture.getV(vertexIndex)));
    }

    private void rotateVertex(Vector3f position, @Nullable ModelRotation rotation) {
        if (rotation != null) {
            Vector3f axis;
            Vector3f scale;
            switch (rotation.axis()) {
                case X -> {
                    axis = new Vector3f(1.0F, 0.0F, 0.0F);
                    scale = new Vector3f(0.0F, 1.0F, 1.0F);
                }
                case Y -> {
                    axis = new Vector3f(0.0F, 1.0F, 0.0F);
                    scale = new Vector3f(1.0F, 0.0F, 1.0F);
                }
                case Z -> {
                    axis = new Vector3f(0.0F, 0.0F, 1.0F);
                    scale = new Vector3f(1.0F, 1.0F, 0.0F);
                }
                default -> throw new IllegalArgumentException("There are only 3 axes");
            }

            Quaternionf quat = new Quaternionf().rotationAxis(rotation.angle() * ((float)Math.PI / 180F), axis);
            if (rotation.rescale()) {
                if (Math.abs(rotation.angle()) == 22.5F) {
                    scale.mul(MIN_SCALE);
                } else {
                    scale.mul(MAX_SCALE);
                }

                scale.add(1.0F, 1.0F, 1.0F);
            } else {
                scale.set(1.0F, 1.0F, 1.0F);
            }

            this.transformVertex(position, new Vector3f(rotation.origin()), new Matrix4f().rotation(quat), scale);
        }
    }

    public void transformVertex(Vector3f position, AffineTransformation transformation) {
        if (transformation != AffineTransformation.identity()) {
            this.transformVertex(position, new Vector3f(0.5F, 0.5F, 0.5F), transformation.getMatrix(), new Vector3f(1.0F, 1.0F, 1.0F));
        }
    }

    private void transformVertex(Vector3f position, Vector3fc origin, Matrix4fc transformationMatrix, Vector3fc scale) {
        Vector4f vector4f = transformationMatrix.transform(new Vector4f(position.x() - origin.x(), position.y() - origin.y(), position.z() - origin.z(), 1.0F));
        vector4f.mul(new Vector4f(scale, 1));
        position.set(vector4f.x() + origin.x(), vector4f.y() + origin.y(), vector4f.z() + origin.z());
    }

    private static Direction decodeDirection(QuadView quad) {
        Vector3f pos1 = new Vector3f(quad.x(0), quad.y(0), quad.z(0));
        Vector3f pos2 = new Vector3f(quad.x(1), quad.y(1), quad.z(1));
        Vector3f pos3 = new Vector3f(quad.x(2), quad.y(2), quad.z(2));
        Vector3f vector3f4 = new Vector3f(pos1).sub(pos2);
        Vector3f vector3f5 = new Vector3f(pos3).sub(pos2);
        Vector3f vector3f6 = new Vector3f(vector3f5).cross(vector3f4).normalize();
        if (!vector3f6.isFinite()) {
            return Direction.UP;
        } else {
            Direction direction = null;
            float f = 0.0F;

            for(Direction dir : Direction.values()) {
                float g = vector3f6.dot(dir.getFloatVector());
                if (g >= 0.0F && g > f) {
                    f = g;
                    direction = dir;
                }
            }

            if (direction == null) {
                return Direction.UP;
            } else {
                return direction;
            }
        }
    }

    private static float bakeVectorX(int[] rotationMatrix, int cornerIndex) {
        return Float.intBitsToFloat(rotationMatrix[cornerIndex]);
    }

    private static float bakeVectorY(int[] is, int i) {
        return Float.intBitsToFloat(is[i + 1]);
    }

    private static float bakeVectorZ(int[] is, int i) {
        return Float.intBitsToFloat(is[i + 2]);
    }

    private static void encodeDirection(int[] rotationMatrix, Direction direction) {
        int[] is = new int[rotationMatrix.length];
        System.arraycopy(rotationMatrix, 0, is, 0, rotationMatrix.length);
        float[] fs = new float[Direction.values().length];
        fs[CubeFace.DirectionIds.WEST] = 999.0F;
        fs[CubeFace.DirectionIds.DOWN] = 999.0F;
        fs[CubeFace.DirectionIds.NORTH] = 999.0F;
        fs[CubeFace.DirectionIds.EAST] = -999.0F;
        fs[CubeFace.DirectionIds.UP] = -999.0F;
        fs[CubeFace.DirectionIds.SOUTH] = -999.0F;

        for(int i = 0; i < 4; ++i) {
            int j = 8 * i;
            float f = bakeVectorX(is, j);
            float g = bakeVectorY(is, j);
            float h = bakeVectorZ(is, j);
            if (f < fs[CubeFace.DirectionIds.WEST]) {
                fs[CubeFace.DirectionIds.WEST] = f;
            }

            if (g < fs[CubeFace.DirectionIds.DOWN]) {
                fs[CubeFace.DirectionIds.DOWN] = g;
            }

            if (h < fs[CubeFace.DirectionIds.NORTH]) {
                fs[CubeFace.DirectionIds.NORTH] = h;
            }

            if (f > fs[CubeFace.DirectionIds.EAST]) {
                fs[CubeFace.DirectionIds.EAST] = f;
            }

            if (g > fs[CubeFace.DirectionIds.UP]) {
                fs[CubeFace.DirectionIds.UP] = g;
            }

            if (h > fs[CubeFace.DirectionIds.SOUTH]) {
                fs[CubeFace.DirectionIds.SOUTH] = h;
            }
        }

        CubeFace cubeFace = CubeFace.getFace(direction);

        for(int j = 0; j < 4; ++j) {
            int k = 8 * j;
            CubeFace.Corner corner = cubeFace.getCorner(j);
            float h = fs[corner.xSide];
            float l = fs[corner.ySide];
            float m = fs[corner.zSide];
            rotationMatrix[k] = Float.floatToRawIntBits(h);
            rotationMatrix[k + 1] = Float.floatToRawIntBits(l);
            rotationMatrix[k + 2] = Float.floatToRawIntBits(m);

            for(int n = 0; n < 4; ++n) {
                int o = 8 * n;
                float p = bakeVectorX(is, o);
                float q = bakeVectorY(is, o);
                float r = bakeVectorZ(is, o);
                if (MathHelper.approximatelyEquals(h, p) && MathHelper.approximatelyEquals(l, q) && MathHelper.approximatelyEquals(m, r)) {
                    rotationMatrix[k + 4] = is[o + 4];
                    rotationMatrix[k + 4 + 1] = is[o + 4 + 1];
                }
            }
        }

    }
}
