package net.modificationstation.stationapi.api.util.math;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import java.util.Objects;

public final class AffineTransformation {
    private final Matrix4fc matrix;
    public static final Codec<AffineTransformation> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codecs.VECTOR_3F.fieldOf("translation").forGetter((affineTransformation) -> affineTransformation.translation), Codecs.ROTATION.fieldOf("left_rotation").forGetter((affineTransformation) -> affineTransformation.leftRotation), Codecs.VECTOR_3F.fieldOf("scale").forGetter((affineTransformation) -> affineTransformation.scale), Codecs.ROTATION.fieldOf("right_rotation").forGetter((affineTransformation) -> affineTransformation.rightRotation)).apply(instance, AffineTransformation::new));
    public static final Codec<AffineTransformation> ANY_CODEC = Codec.withAlternative(CODEC, Codecs.MATRIX_4F.xmap(AffineTransformation::new, AffineTransformation::copyMatrix));
    private boolean initialized;
    @Nullable
    private Vector3f translation;
    @Nullable
    private Quaternionf leftRotation;
    @Nullable
    private Vector3f scale;
    @Nullable
    private Quaternionf rightRotation;
    private static final AffineTransformation IDENTITY = Util.make(() -> {
        AffineTransformation affineTransformation = new AffineTransformation(new Matrix4f());
        affineTransformation.translation = new Vector3f();
        affineTransformation.leftRotation = new Quaternionf();
        affineTransformation.scale = new Vector3f(1.0F, 1.0F, 1.0F);
        affineTransformation.rightRotation = new Quaternionf();
        affineTransformation.initialized = true;
        return affineTransformation;
    });

    public AffineTransformation(@Nullable Matrix4fc matrix) {
        if (matrix == null) {
            this.matrix = IDENTITY.matrix;
        } else {
            this.matrix = matrix;
        }

    }

    public AffineTransformation(@Nullable Vector3f translation, @Nullable Quaternionf leftRotation, @Nullable Vector3f scale, @Nullable Quaternionf rightRotation) {
        this.matrix = setup(translation, leftRotation, scale, rightRotation);
        this.translation = translation != null ? translation : new Vector3f();
        this.leftRotation = leftRotation != null ? leftRotation : new Quaternionf();
        this.scale = scale != null ? scale : new Vector3f(1.0F, 1.0F, 1.0F);
        this.rightRotation = rightRotation != null ? rightRotation : new Quaternionf();
        this.initialized = true;
    }

    public static AffineTransformation identity() {
        return IDENTITY;
    }

    public AffineTransformation multiply(AffineTransformation other) {
        Matrix4f matrix4f = this.copyMatrix();
        matrix4f.mul(other.getMatrix());
        return new AffineTransformation(matrix4f);
    }

    @Nullable
    public AffineTransformation invert() {
        if (this == IDENTITY) {
            return this;
        } else {
            Matrix4f matrix4f = this.copyMatrix().invertAffine();
            return matrix4f.isFinite() ? new AffineTransformation(matrix4f) : null;
        }
    }

    private void init() {
        if (!this.initialized) {
            float scale = 1.0F / this.matrix.m33();
            Triple<Quaternionf, Vector3f, Quaternionf> triple = MatrixUtil.svdDecompose(new Matrix3f(this.matrix).scale(scale));
            this.translation = this.matrix.getTranslation(new Vector3f()).mul(scale);
            this.leftRotation = new Quaternionf(triple.getLeft());
            this.scale = new Vector3f(triple.getMiddle());
            this.rightRotation = new Quaternionf(triple.getRight());
            this.initialized = true;
        }

    }

    private static Matrix4f setup(@Nullable Vector3f translation, @Nullable Quaternionf leftRotation, @Nullable Vector3f scale, @Nullable Quaternionf rightRotation) {
        Matrix4f matrix4f = new Matrix4f();
        if (translation != null) {
            matrix4f.translation(translation);
        }

        if (leftRotation != null) {
            matrix4f.rotate(leftRotation);
        }

        if (scale != null) {
            matrix4f.scale(scale);
        }

        if (rightRotation != null) {
            matrix4f.rotate(rightRotation);
        }

        return matrix4f;
    }

    public Matrix4fc getMatrix() {
        return this.matrix;
    }

    public Matrix4f copyMatrix() {
        return new Matrix4f(this.matrix);
    }

    public Vector3f getTranslation() {
        this.init();
        return new Vector3f(this.translation);
    }

    public Quaternionf getLeftRotation() {
        this.init();
        return new Quaternionf(this.leftRotation);
    }

    public Vector3f getScale() {
        this.init();
        return new Vector3f(this.scale);
    }

    public Quaternionf getRightRotation() {
        this.init();
        return new Quaternionf(this.rightRotation);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            AffineTransformation affineTransformation = (AffineTransformation)object;
            return Objects.equals(this.matrix, affineTransformation.matrix);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.matrix);
    }

    public AffineTransformation interpolate(AffineTransformation target, float factor) {
        Vector3f vector3f = this.getTranslation();
        Quaternionf quaternionf = this.getLeftRotation();
        Vector3f vector3f2 = this.getScale();
        Quaternionf quaternionf2 = this.getRightRotation();
        vector3f.lerp(target.getTranslation(), factor);
        quaternionf.slerp(target.getLeftRotation(), factor);
        vector3f2.lerp(target.getScale(), factor);
        quaternionf2.slerp(target.getRightRotation(), factor);
        return new AffineTransformation(vector3f, quaternionf, vector3f2, quaternionf2);
    }
}
