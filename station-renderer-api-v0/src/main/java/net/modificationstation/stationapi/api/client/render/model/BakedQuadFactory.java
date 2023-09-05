package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElementFace;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElementTexture;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.math.*;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class BakedQuadFactory {
    private static final float MIN_SCALE = 1.0F / (float)Math.cos(0.39269909262657166D) - 1.0F;
    private static final float MAX_SCALE = 1.0F / (float)Math.cos(0.7853981852531433D) - 1.0F;

    public BakedQuad bake(Vec3f from, Vec3f to, ModelElementFace face, Sprite texture, Direction side, ModelBakeSettings settings, @Nullable ModelRotation rotation, boolean shade, Identifier modelId) {
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
        int[] is = this.packVertexData(modelElementTexture, texture, side, this.getPositionMatrix(from, to), settings.getRotation(), rotation, shade);
        Direction direction = decodeDirection(is);
        System.arraycopy(fs, 0, modelElementTexture.uvs, 0, fs.length);
        if (rotation == null) {
            this.encodeDirection(is, direction);
        }

        return new BakedQuad(is, face.tintIndex, direction, texture, shade);
    }

    public static ModelElementTexture uvLock(ModelElementTexture texture, Direction orientation, AffineTransformation rotation, Identifier modelId) {
        Matrix4f matrix4f = AffineTransformations.uvLock(rotation, orientation, () -> "Unable to resolve UVLock for model: " + modelId).getMatrix();
        float f = texture.getU(texture.getDirectionIndex(0));
        float g = texture.getV(texture.getDirectionIndex(0));
        Vector4f vector4f = new Vector4f(f / 16.0F, g / 16.0F, 0.0F, 1.0F);
        vector4f.transform(matrix4f);
        float h = 16.0F * vector4f.getX();
        float i = 16.0F * vector4f.getY();
        float j = texture.getU(texture.getDirectionIndex(2));
        float k = texture.getV(texture.getDirectionIndex(2));
        Vector4f vector4f2 = new Vector4f(j / 16.0F, k / 16.0F, 0.0F, 1.0F);
        vector4f2.transform(matrix4f);
        float l = 16.0F * vector4f2.getX();
        float m = 16.0F * vector4f2.getY();
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
        Vec3f vector3f = new Vec3f(net.minecraft.util.maths.MathHelper.cos(v), net.minecraft.util.maths.MathHelper.sin(v), 0.0F);
        Matrix3f matrix3f = new Matrix3f(matrix4f);
        vector3f.transform(matrix3f);
        int w = Math.floorMod(-((int)Math.round(Math.toDegrees(Math.atan2(vector3f.getY(), vector3f.getX())) / 90.0D)) * 90, 360);
        return new ModelElementTexture(new float[]{p, t, q, u}, w);
    }

    private int[] packVertexData(ModelElementTexture texture, Sprite sprite, Direction direction, float[] positionMatrix, AffineTransformation orientation, @Nullable ModelRotation rotation, boolean shaded) {
        int[] is = new int[32];

        for(int i = 0; i < 4; ++i) {
            this.packVertexData(is, i, direction, texture, positionMatrix, sprite, orientation, rotation, shaded);
        }

        return is;
    }

    private float[] getPositionMatrix(Vec3f from, Vec3f to) {
        float[] fs = new float[Direction.values().length];
        fs[CubeFace.DirectionIds.EAST] = from.getZ() / 16.0F;
        fs[CubeFace.DirectionIds.DOWN] = from.getY() / 16.0F;
        fs[CubeFace.DirectionIds.NORTH] = from.getX() / 16.0F;
        fs[CubeFace.DirectionIds.WEST] = to.getZ() / 16.0F;
        fs[CubeFace.DirectionIds.UP] = to.getY() / 16.0F;
        fs[CubeFace.DirectionIds.SOUTH] = to.getX() / 16.0F;
        return fs;
    }

    private void packVertexData(int[] vertices, int cornerIndex, Direction direction, ModelElementTexture texture, float[] positionMatrix, Sprite sprite, AffineTransformation orientation, @Nullable ModelRotation rotation, boolean shaded) {
        CubeFace.Corner corner = CubeFace.getFace(direction).getCorner(cornerIndex);
        Vec3f vector3f = new Vec3f(positionMatrix[corner.xSide], positionMatrix[corner.ySide], positionMatrix[corner.zSide]);
        this.rotateVertex(vector3f, rotation);
        this.transformVertex(vector3f, orientation);
        this.packVertexData(vertices, cornerIndex, vector3f, sprite, texture);
    }

    private void packVertexData(int[] vertices, int cornerIndex, Vec3f position, Sprite sprite, ModelElementTexture modelElementTexture) {
        int i = cornerIndex * 8;
        vertices[i] = Float.floatToRawIntBits(position.getX());
        vertices[i + 1] = Float.floatToRawIntBits(position.getY());
        vertices[i + 2] = Float.floatToRawIntBits(position.getZ());
//      vertices[i + 3] = -1;
        vertices[i + 3] = Float.floatToRawIntBits(sprite.getFrameU(modelElementTexture.getU(cornerIndex)));
        vertices[i + 3 + 1] = Float.floatToRawIntBits(sprite.getFrameV(modelElementTexture.getV(cornerIndex)));
    }

    private void rotateVertex(Vec3f vector, @Nullable ModelRotation rotation) {
        if (rotation != null) {
            Vec3f vector3f7;
            Vec3f vector3f8;
            switch (rotation.axis()) {
                case X -> {
                    vector3f7 = new Vec3f(1.0F, 0.0F, 0.0F);
                    vector3f8 = new Vec3f(0.0F, 1.0F, 1.0F);
                }
                case Y -> {
                    vector3f7 = new Vec3f(0.0F, 1.0F, 0.0F);
                    vector3f8 = new Vec3f(1.0F, 0.0F, 1.0F);
                }
                case Z -> {
                    vector3f7 = new Vec3f(0.0F, 0.0F, 1.0F);
                    vector3f8 = new Vec3f(1.0F, 1.0F, 0.0F);
                }
                default -> throw new IllegalArgumentException("There are only 3 axes");
            }

            Quaternion quaternion = new Quaternion(vector3f7, rotation.angle(), true);
            if (rotation.rescale()) {
                if (Math.abs(rotation.angle()) == 22.5F) {
                    vector3f8.scale(MIN_SCALE);
                } else {
                    vector3f8.scale(MAX_SCALE);
                }

                vector3f8.add(1.0F, 1.0F, 1.0F);
            } else {
                vector3f8.set(1.0F, 1.0F, 1.0F);
            }

            this.transformVertex(vector, rotation.origin().copy(), new Matrix4f(quaternion), vector3f8);
        }
    }

    public void transformVertex(Vec3f vertex, AffineTransformation transformation) {
        if (transformation != AffineTransformation.identity()) {
            this.transformVertex(vertex, new Vec3f(0.5F, 0.5F, 0.5F), transformation.getMatrix(), new Vec3f(1.0F, 1.0F, 1.0F));
        }
    }

    private void transformVertex(Vec3f vertex, Vec3f origin, Matrix4f transformationMatrix, Vec3f scale) {
        Vector4f vector4f = new Vector4f(vertex.getX() - origin.getX(), vertex.getY() - origin.getY(), vertex.getZ() - origin.getZ(), 1.0F);
        vector4f.transform(transformationMatrix);
        vector4f.multiplyComponentwise(scale);
        vertex.set(vector4f.getX() + origin.getX(), vector4f.getY() + origin.getY(), vector4f.getZ() + origin.getZ());
    }

    public static Direction decodeDirection(int[] rotationMatrix) {
        Vec3f vector3f = new Vec3f(Float.intBitsToFloat(rotationMatrix[0]), Float.intBitsToFloat(rotationMatrix[1]), Float.intBitsToFloat(rotationMatrix[2]));
        Vec3f vector3f2 = new Vec3f(Float.intBitsToFloat(rotationMatrix[8]), Float.intBitsToFloat(rotationMatrix[9]), Float.intBitsToFloat(rotationMatrix[10]));
        Vec3f vector3f3 = new Vec3f(Float.intBitsToFloat(rotationMatrix[16]), Float.intBitsToFloat(rotationMatrix[17]), Float.intBitsToFloat(rotationMatrix[18]));
        Vec3f vector3f4 = vector3f.copy();
        vector3f4.subtract(vector3f2);
        Vec3f vector3f5 = vector3f3.copy();
        vector3f5.subtract(vector3f2);
        Vec3f vector3f6 = vector3f5.copy();
        vector3f6.cross(vector3f4);
        vector3f6.normalize();
        Direction direction = null;
        float f = 0.0F;
        Direction[] var9 = Direction.values();

        for (Direction direction2 : var9) {
            Vec3f vector3f7 = direction2.getUnitVector();
            float g = vector3f6.dot(vector3f7);
            if (g >= 0.0F && g > f) {
                f = g;
                direction = direction2;
            }
        }

        return Objects.requireNonNullElse(direction, Direction.UP);
    }

    private void encodeDirection(int[] rotationMatrix, Direction direction) {
        int[] is = new int[rotationMatrix.length];
        System.arraycopy(rotationMatrix, 0, is, 0, rotationMatrix.length);
        float[] fs = new float[Direction.values().length];
        fs[CubeFace.DirectionIds.NORTH] = 999.0F;
        fs[CubeFace.DirectionIds.DOWN] = 999.0F;
        fs[CubeFace.DirectionIds.EAST] = 999.0F;
        fs[CubeFace.DirectionIds.SOUTH] = -999.0F;
        fs[CubeFace.DirectionIds.UP] = -999.0F;
        fs[CubeFace.DirectionIds.WEST] = -999.0F;

        int k;
        float h;
        for(int i = 0; i < 4; ++i) {
            k = 8 * i;
            float f = Float.intBitsToFloat(is[k]);
            float g = Float.intBitsToFloat(is[k + 1]);
            h = Float.intBitsToFloat(is[k + 2]);
            if (f < fs[CubeFace.DirectionIds.NORTH]) {
                fs[CubeFace.DirectionIds.NORTH] = f;
            }

            if (g < fs[CubeFace.DirectionIds.DOWN]) {
                fs[CubeFace.DirectionIds.DOWN] = g;
            }

            if (h < fs[CubeFace.DirectionIds.EAST]) {
                fs[CubeFace.DirectionIds.EAST] = h;
            }

            if (f > fs[CubeFace.DirectionIds.SOUTH]) {
                fs[CubeFace.DirectionIds.SOUTH] = f;
            }

            if (g > fs[CubeFace.DirectionIds.UP]) {
                fs[CubeFace.DirectionIds.UP] = g;
            }

            if (h > fs[CubeFace.DirectionIds.WEST]) {
                fs[CubeFace.DirectionIds.WEST] = h;
            }
        }

        CubeFace cubeFace = CubeFace.getFace(direction);

        for(k = 0; k < 4; ++k) {
            int l = 8 * k;
            CubeFace.Corner corner = cubeFace.getCorner(k);
            h = fs[corner.xSide];
            float n = fs[corner.ySide];
            float o = fs[corner.zSide];
            rotationMatrix[l] = Float.floatToRawIntBits(h);
            rotationMatrix[l + 1] = Float.floatToRawIntBits(n);
            rotationMatrix[l + 2] = Float.floatToRawIntBits(o);

            for(int p = 0; p < 4; ++p) {
                int q = 8 * p;
                float r = Float.intBitsToFloat(is[q]);
                float s = Float.intBitsToFloat(is[q + 1]);
                float t = Float.intBitsToFloat(is[q + 2]);
                if (MathHelper.approximatelyEquals(h, r) && MathHelper.approximatelyEquals(n, s) && MathHelper.approximatelyEquals(o, t)) {
                    rotationMatrix[l + 3] = is[q + 3];
                    rotationMatrix[l + 3 + 1] = is[q + 3 + 1];
                }
            }
        }

    }
}
