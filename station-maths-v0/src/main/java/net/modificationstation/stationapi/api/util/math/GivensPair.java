package net.modificationstation.stationapi.api.util.math;

import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Quaternionf;

public record GivensPair(float sinHalf, float cosHalf) {
    public static GivensPair normalize(float a, float b) {
        float n = Math.invsqrt(a * a + b * b);
        return new GivensPair(n * a, n * b);
    }

    public static GivensPair fromAngle(float radians) {
        float sin = Math.sin(radians / 2.0F);
        float cos = Math.cosFromSin(sin, radians / 2.0F);
        return new GivensPair(sin, cos);
    }

    public GivensPair negateSin() {
        return new GivensPair(-this.sinHalf, this.cosHalf);
    }

    public Quaternionf setXRotation(Quaternionf quaternionf) {
        return quaternionf.set(this.sinHalf, 0.0F, 0.0F, this.cosHalf);
    }

    public Quaternionf setYRotation(Quaternionf quaternionf) {
        return quaternionf.set(0.0F, this.sinHalf, 0.0F, this.cosHalf);
    }

    public Quaternionf setZRotation(Quaternionf quaternionf) {
        return quaternionf.set(0.0F, 0.0F, this.sinHalf, this.cosHalf);
    }

    public float cosDouble() {
        return this.cosHalf * this.cosHalf - this.sinHalf * this.sinHalf;
    }

    public float sinDouble() {
        return 2.0F * this.sinHalf * this.cosHalf;
    }

    public Matrix3f setRotationX(Matrix3f matrix3f) {
        matrix3f.m01 = 0.0F;
        matrix3f.m02 = 0.0F;
        matrix3f.m10 = 0.0F;
        matrix3f.m20 = 0.0F;
        float cos = this.cosDouble();
        float sin = this.sinDouble();
        matrix3f.m11 = cos;
        matrix3f.m22 = cos;
        matrix3f.m12 = sin;
        matrix3f.m21 = -sin;
        matrix3f.m00 = 1.0F;
        return matrix3f;
    }

    public Matrix3f setRotationY(Matrix3f matrix3f) {
        matrix3f.m01 = 0.0F;
        matrix3f.m10 = 0.0F;
        matrix3f.m12 = 0.0F;
        matrix3f.m21 = 0.0F;
        float cos = this.cosDouble();
        float sin = this.sinDouble();
        matrix3f.m00 = cos;
        matrix3f.m22 = cos;
        matrix3f.m02 = -sin;
        matrix3f.m20 = sin;
        matrix3f.m11 = 1.0F;
        return matrix3f;
    }

    public Matrix3f setRotationZ(Matrix3f matrix3f) {
        matrix3f.m02 = 0.0F;
        matrix3f.m12 = 0.0F;
        matrix3f.m20 = 0.0F;
        matrix3f.m21 = 0.0F;
        float cos = this.cosDouble();
        float sin = this.sinDouble();
        matrix3f.m00 = cos;
        matrix3f.m11 = cos;
        matrix3f.m01 = sin;
        matrix3f.m10 = -sin;
        matrix3f.m22 = 1.0F;
        return matrix3f;
    }

    public float sinHalf() {
        return this.sinHalf;
    }

    public float cosHalf() {
        return this.cosHalf;
    }
}