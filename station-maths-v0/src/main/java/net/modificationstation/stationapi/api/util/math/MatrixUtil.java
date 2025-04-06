package net.modificationstation.stationapi.api.util.math;

import org.apache.commons.lang3.tuple.Triple;
import org.joml.*;
import org.joml.Math;

public class MatrixUtil {
    private static final float COT_PI_OVER_8 = 3.0F + 2.0F * Math.sqrt(2.0F);
    private static final GivensPair SIN_COS_PI_OVER_8 = GivensPair.fromAngle(((float) Math.PI / 4F));

    public static Matrix4f scale(Matrix4f matrix, float scalar) {
        return matrix.set(matrix.m00() * scalar, matrix.m01() * scalar, matrix.m02() * scalar, matrix.m03() * scalar, matrix.m10() * scalar, matrix.m11() * scalar, matrix.m12() * scalar, matrix.m13() * scalar, matrix.m20() * scalar, matrix.m21() * scalar, matrix.m22() * scalar, matrix.m23() * scalar, matrix.m30() * scalar, matrix.m31() * scalar, matrix.m32() * scalar, matrix.m33() * scalar);
    }

    private static GivensPair approximateGivensQuaternion(float a11, float a12, float a22) {
        float f = 2.0F * (a11 - a22);
        return COT_PI_OVER_8 * a12 * a12 < f * f ? GivensPair.normalize(a12, f) : SIN_COS_PI_OVER_8;
    }

    private static GivensPair qrGivensQuaternion(float a1, float a2) {
        float f = (float) java.lang.Math.hypot((double) a1, (double) a2);
        float g = f > 1.0E-6F ? a2 : 0.0F;
        float h = Math.abs(a1) + Math.max(f, 1.0E-6F);
        if (a1 < 0.0F) {
            float i = g;
            g = h;
            h = i;
        }

        return GivensPair.normalize(g, h);
    }

    private static void conjugate(Matrix3f X, Matrix3f A) {
        X.mul(A);
        A.transpose();
        A.mul(X);
        X.set(A);
    }

    private static void applyJacobiIteration(Matrix3f AtA, Matrix3f matrix3f, Quaternionf quaternionf, Quaternionf quaternionf2) {
        if (AtA.m01 * AtA.m01 + AtA.m10 * AtA.m10 > 1.0E-6F) {
            GivensPair givensPair = approximateGivensQuaternion(AtA.m00, 0.5F * (AtA.m01 + AtA.m10), AtA.m11);
            Quaternionf quaternionf3 = givensPair.setZRotation(quaternionf);
            quaternionf2.mul(quaternionf3);
            givensPair.setRotationZ(matrix3f);
            conjugate(AtA, matrix3f);
        }

        if (AtA.m02 * AtA.m02 + AtA.m20 * AtA.m20 > 1.0E-6F) {
            GivensPair givensPair = approximateGivensQuaternion(AtA.m00, 0.5F * (AtA.m02 + AtA.m20), AtA.m22).negateSin();
            Quaternionf quaternionf3 = givensPair.setYRotation(quaternionf);
            quaternionf2.mul(quaternionf3);
            givensPair.setRotationY(matrix3f);
            conjugate(AtA, matrix3f);
        }

        if (AtA.m12 * AtA.m12 + AtA.m21 * AtA.m21 > 1.0E-6F) {
            GivensPair givensPair = approximateGivensQuaternion(AtA.m11, 0.5F * (AtA.m12 + AtA.m21), AtA.m22);
            Quaternionf quaternionf3 = givensPair.setXRotation(quaternionf);
            quaternionf2.mul(quaternionf3);
            givensPair.setRotationX(matrix3f);
            conjugate(AtA, matrix3f);
        }

    }

    public static Quaternionf applyJacobiIterations(Matrix3f AtA, int numJacobiIterations) {
        Quaternionf quaternionf = new Quaternionf();
        Matrix3f matrix3f = new Matrix3f();
        Quaternionf quaternionf2 = new Quaternionf();

        for (int i = 0; i < numJacobiIterations; ++i) {
            applyJacobiIteration(AtA, matrix3f, quaternionf2, quaternionf);
        }

        quaternionf.normalize();
        return quaternionf;
    }

    public static Triple<Quaternionf, Vector3f, Quaternionf> svdDecompose(Matrix3f A) {
        Matrix3f matrix3f = new Matrix3f(A);
        matrix3f.transpose();
        matrix3f.mul(A);
        Quaternionf quaternionf = applyJacobiIterations(matrix3f, 5);
        float f = matrix3f.m00;
        float g = matrix3f.m11;
        boolean bl = (double) f < 1.0E-6;
        boolean bl2 = (double) g < 1.0E-6;
        Matrix3f matrix3f3 = A.rotate(quaternionf);
        Quaternionf quaternionf2 = new Quaternionf();
        Quaternionf quaternionf3 = new Quaternionf();
        GivensPair givensPair;
        if (bl) {
            givensPair = qrGivensQuaternion(matrix3f3.m11, -matrix3f3.m10);
        } else {
            givensPair = qrGivensQuaternion(matrix3f3.m00, matrix3f3.m01);
        }

        Quaternionf quaternionf4 = givensPair.setZRotation(quaternionf3);
        Matrix3f matrix3f4 = givensPair.setRotationZ(matrix3f);
        quaternionf2.mul(quaternionf4);
        matrix3f4.transpose().mul(matrix3f3);
        if (bl) {
            givensPair = qrGivensQuaternion(matrix3f4.m22, -matrix3f4.m20);
        } else {
            givensPair = qrGivensQuaternion(matrix3f4.m00, matrix3f4.m02);
        }

        givensPair = givensPair.negateSin();
        Quaternionf quaternionf5 = givensPair.setYRotation(quaternionf3);
        Matrix3f matrix3f5 = givensPair.setRotationY(matrix3f3);
        quaternionf2.mul(quaternionf5);
        matrix3f5.transpose().mul(matrix3f4);
        if (bl2) {
            givensPair = qrGivensQuaternion(matrix3f5.m22, -matrix3f5.m21);
        } else {
            givensPair = qrGivensQuaternion(matrix3f5.m11, matrix3f5.m12);
        }

        Quaternionf quaternionf6 = givensPair.setXRotation(quaternionf3);
        Matrix3f matrix3f6 = givensPair.setRotationX(matrix3f4);
        quaternionf2.mul(quaternionf6);
        matrix3f6.transpose().mul(matrix3f5);
        Vector3f vector3f = new Vector3f(matrix3f6.m00, matrix3f6.m11, matrix3f6.m22);
        return Triple.of(quaternionf2, vector3f, quaternionf.conjugate());
    }

    private static boolean isPropertyBitSet(Matrix4fc matrix, int property) {
        return (matrix.properties() & property) != 0;
    }

    public static boolean hasProperty(Matrix4fc matrix, int property) {
        if (isPropertyBitSet(matrix, property)) {
            return true;
        } else if (matrix instanceof Matrix4f) {
            Matrix4f matrix4f = (Matrix4f) matrix;
            matrix4f.determineProperties();
            return isPropertyBitSet(matrix, property);
        } else {
            return false;
        }
    }

    public static boolean isIdentity(Matrix4fc matrix) {
        return hasProperty(matrix, 4);
    }

    public static boolean isTranslation(Matrix4fc matrix) {
        return hasProperty(matrix, 8);
    }

    public static boolean isOrthonormal(Matrix4fc matrix) {
        return hasProperty(matrix, 16);
    }
}
