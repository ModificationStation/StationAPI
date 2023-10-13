package net.modificationstation.stationapi.api.util.math;

import org.apache.commons.lang3.tuple.Triple;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

import java.nio.FloatBuffer;

public final class Matrix3f {
    private static final float THREE_PLUS_TWO_SQRT_TWO = 3.0F + 2.0F * (float)Math.sqrt(2.0D);
    private static final float COS_PI_OVER_EIGHT = (float)Math.cos(0.39269908169872414D);
    private static final float SIN_PI_OVER_EIGHT = (float)Math.sin(0.39269908169872414D);
    float a00;
    float a01;
    float a02;
    float a10;
    float a11;
    float a12;
    float a20;
    float a21;
    float a22;

    public Matrix3f() {}

    public Matrix3f(Quaternion source) {
        float f = source.getX();
        float g = source.getY();
        float h = source.getZ();
        float i = source.getW();
        float j = 2.0F * f * f;
        float k = 2.0F * g * g;
        float l = 2.0F * h * h;
        this.a00 = 1.0F - k - l;
        this.a11 = 1.0F - l - j;
        this.a22 = 1.0F - j - k;
        float m = f * g;
        float n = g * h;
        float o = h * f;
        float p = f * i;
        float q = g * i;
        float r = h * i;
        this.a10 = 2.0F * (m + r);
        this.a01 = 2.0F * (m - r);
        this.a20 = 2.0F * (o - q);
        this.a02 = 2.0F * (o + q);
        this.a21 = 2.0F * (n + p);
        this.a12 = 2.0F * (n - p);
    }

    public static Matrix3f scale(float x, float y, float z) {
        Matrix3f matrix3f = new Matrix3f();
        matrix3f.a00 = x;
        matrix3f.a11 = y;
        matrix3f.a22 = z;
        return matrix3f;
    }

    public static Matrix3f scaleTmp(float x, float y, float z) {
        tmpMatrix.loadIdentity();
        tmpMatrix.a00 = x;
        tmpMatrix.a11 = y;
        tmpMatrix.a22 = z;
        return tmpMatrix;
    }

    private static final Matrix3f tmpMatrix = new Matrix3f();

    public Matrix3f(Matrix4f source) {
        this.a00 = source.a00;
        this.a01 = source.a01;
        this.a02 = source.a02;
        this.a10 = source.a10;
        this.a11 = source.a11;
        this.a12 = source.a12;
        this.a20 = source.a20;
        this.a21 = source.a21;
        this.a22 = source.a22;
    }

    public Matrix3f(Matrix3f source) {
        this.a00 = source.a00;
        this.a01 = source.a01;
        this.a02 = source.a02;
        this.a10 = source.a10;
        this.a11 = source.a11;
        this.a12 = source.a12;
        this.a20 = source.a20;
        this.a21 = source.a21;
        this.a22 = source.a22;
    }

    private static BiTuple<Float, Float> getSinAndCosOfRotation(float upperLeft, float diagonalAverage, float lowerRight) {
        float f = 2.0F * (upperLeft - lowerRight);
        if (THREE_PLUS_TWO_SQRT_TWO * diagonalAverage * diagonalAverage < f * f) {
            float h = MathHelper.fastInverseSqrt(diagonalAverage * diagonalAverage + f * f);
            return Tuple.tuple(h * diagonalAverage, h * f);
        } else {
            return Tuple.tuple(SIN_PI_OVER_EIGHT, COS_PI_OVER_EIGHT);
        }
    }

    private static BiTuple<Float, Float> method_22848(float f, float g) {
        float h = (float)Math.hypot(f, g);
        float i = h > 1.0E-6F ? g : 0.0F;
        float j = Math.abs(f) + Math.max(h, 1.0E-6F);
        float l;
        if (f < 0.0F) {
            l = i;
            i = j;
            j = l;
        }

        l = MathHelper.fastInverseSqrt(j * j + i * i);
        j *= l;
        i *= l;
        return Tuple.tuple(i, j);
    }

    private static Quaternion method_22857(Matrix3f matrix3f) {
        Matrix3f matrix3f2 = new Matrix3f();
        Quaternion quaternion = Quaternion.IDENTITY.copy();
        BiTuple<Float, Float> pair3;
        Float float4;
        Float float5;
        Quaternion quaternion4;
        float m;
        float n;
        float o;
        if (matrix3f.a01 * matrix3f.a01 + matrix3f.a10 * matrix3f.a10 > 1.0E-6F) {
            pair3 = getSinAndCosOfRotation(matrix3f.a00, 0.5F * (matrix3f.a01 + matrix3f.a10), matrix3f.a11);
            float4 = pair3.one();
            float5 = pair3.two();
            quaternion4 = new Quaternion(0.0F, 0.0F, float4, float5);
            m = float5 * float5 - float4 * float4;
            n = -2.0F * float4 * float5;
            o = float5 * float5 + float4 * float4;
            quaternion.hamiltonProduct(quaternion4);
            matrix3f2.loadIdentity();
            matrix3f2.a00 = m;
            matrix3f2.a11 = m;
            matrix3f2.a10 = -n;
            matrix3f2.a01 = n;
            matrix3f2.a22 = o;
            matrix3f.multiply(matrix3f2);
            matrix3f2.transpose();
            matrix3f2.multiply(matrix3f);
            matrix3f.load(matrix3f2);
        }

        if (matrix3f.a02 * matrix3f.a02 + matrix3f.a20 * matrix3f.a20 > 1.0E-6F) {
            pair3 = getSinAndCosOfRotation(matrix3f.a00, 0.5F * (matrix3f.a02 + matrix3f.a20), matrix3f.a22);
            float i = -(Float)pair3.one();
            float5 = pair3.two();
            quaternion4 = new Quaternion(0.0F, i, 0.0F, float5);
            m = float5 * float5 - i * i;
            n = -2.0F * i * float5;
            o = float5 * float5 + i * i;
            quaternion.hamiltonProduct(quaternion4);
            matrix3f2.loadIdentity();
            matrix3f2.a00 = m;
            matrix3f2.a22 = m;
            matrix3f2.a20 = n;
            matrix3f2.a02 = -n;
            matrix3f2.a11 = o;
            matrix3f.multiply(matrix3f2);
            matrix3f2.transpose();
            matrix3f2.multiply(matrix3f);
            matrix3f.load(matrix3f2);
        }

        if (matrix3f.a12 * matrix3f.a12 + matrix3f.a21 * matrix3f.a21 > 1.0E-6F) {
            pair3 = getSinAndCosOfRotation(matrix3f.a11, 0.5F * (matrix3f.a12 + matrix3f.a21), matrix3f.a22);
            float4 = pair3.one();
            float5 = pair3.two();
            quaternion4 = new Quaternion(float4, 0.0F, 0.0F, float5);
            m = float5 * float5 - float4 * float4;
            n = -2.0F * float4 * float5;
            o = float5 * float5 + float4 * float4;
            quaternion.hamiltonProduct(quaternion4);
            matrix3f2.loadIdentity();
            matrix3f2.a11 = m;
            matrix3f2.a22 = m;
            matrix3f2.a21 = -n;
            matrix3f2.a12 = n;
            matrix3f2.a00 = o;
            matrix3f.multiply(matrix3f2);
            matrix3f2.transpose();
            matrix3f2.multiply(matrix3f);
            matrix3f.load(matrix3f2);
        }

        return quaternion;
    }

    public void transpose() {
        float f = this.a01;
        this.a01 = this.a10;
        this.a10 = f;
        f = this.a02;
        this.a02 = this.a20;
        this.a20 = f;
        f = this.a12;
        this.a12 = this.a21;
        this.a21 = f;
    }

    public Triple<Quaternion, Vec3f, Quaternion> decomposeLinearTransformation() {
        Quaternion quaternion = Quaternion.IDENTITY.copy();
        Quaternion quaternion2 = Quaternion.IDENTITY.copy();
        Matrix3f matrix3f = this.copy();
        matrix3f.transpose();
        matrix3f.multiply(this);

        for(int i = 0; i < 5; ++i) {
            quaternion2.hamiltonProduct(method_22857(matrix3f));
        }

        quaternion2.normalize();
        Matrix3f matrix3f2 = new Matrix3f(this);
        matrix3f2.multiply(new Matrix3f(quaternion2));
        float f = 1.0F;
        BiTuple<Float, Float> pair = method_22848(matrix3f2.a00, matrix3f2.a10);
        Float float_ = pair.one();
        Float float2 = pair.two();
        float g = float2 * float2 - float_ * float_;
        float h = -2.0F * float_ * float2;
        float j = float2 * float2 + float_ * float_;
        Quaternion quaternion3 = new Quaternion(0.0F, 0.0F, float_, float2);
        quaternion.hamiltonProduct(quaternion3);
        Matrix3f matrix3f3 = new Matrix3f();
        matrix3f3.loadIdentity();
        matrix3f3.a00 = g;
        matrix3f3.a11 = g;
        matrix3f3.a10 = h;
        matrix3f3.a01 = -h;
        matrix3f3.a22 = j;
        f *= j;
        matrix3f3.multiply(matrix3f2);
        pair = method_22848(matrix3f3.a00, matrix3f3.a20);
        float k = -(Float)pair.one();
        Float float3 = pair.two();
        float l = float3 * float3 - k * k;
        float m = -2.0F * k * float3;
        float n = float3 * float3 + k * k;
        Quaternion quaternion4 = new Quaternion(0.0F, k, 0.0F, float3);
        quaternion.hamiltonProduct(quaternion4);
        Matrix3f matrix3f4 = new Matrix3f();
        matrix3f4.loadIdentity();
        matrix3f4.a00 = l;
        matrix3f4.a22 = l;
        matrix3f4.a20 = -m;
        matrix3f4.a02 = m;
        matrix3f4.a11 = n;
        f *= n;
        matrix3f4.multiply(matrix3f3);
        pair = method_22848(matrix3f4.a11, matrix3f4.a21);
        Float float4 = pair.one();
        Float float5 = pair.two();
        float o = float5 * float5 - float4 * float4;
        float p = -2.0F * float4 * float5;
        float q = float5 * float5 + float4 * float4;
        Quaternion quaternion5 = new Quaternion(float4, 0.0F, 0.0F, float5);
        quaternion.hamiltonProduct(quaternion5);
        Matrix3f matrix3f5 = new Matrix3f();
        matrix3f5.loadIdentity();
        matrix3f5.a11 = o;
        matrix3f5.a22 = o;
        matrix3f5.a21 = p;
        matrix3f5.a12 = -p;
        matrix3f5.a00 = q;
        f *= q;
        matrix3f5.multiply(matrix3f4);
        f = 1.0F / f;
        quaternion.scale((float)Math.sqrt(f));
        Vec3f vector3f = new Vec3f(matrix3f5.a00 * f, matrix3f5.a11 * f, matrix3f5.a22 * f);
        return Triple.of(quaternion, vector3f, quaternion2);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            Matrix3f matrix3f = (Matrix3f)object;
            return Float.compare(matrix3f.a00, this.a00) == 0 && Float.compare(matrix3f.a01, this.a01) == 0 && Float.compare(matrix3f.a02, this.a02) == 0 && Float.compare(matrix3f.a10, this.a10) == 0 && Float.compare(matrix3f.a11, this.a11) == 0 && Float.compare(matrix3f.a12, this.a12) == 0 && Float.compare(matrix3f.a20, this.a20) == 0 && Float.compare(matrix3f.a21, this.a21) == 0 && Float.compare(matrix3f.a22, this.a22) == 0;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = this.a00 != 0.0F ? Float.floatToIntBits(this.a00) : 0;
        i = 31 * i + (this.a01 != 0.0F ? Float.floatToIntBits(this.a01) : 0);
        i = 31 * i + (this.a02 != 0.0F ? Float.floatToIntBits(this.a02) : 0);
        i = 31 * i + (this.a10 != 0.0F ? Float.floatToIntBits(this.a10) : 0);
        i = 31 * i + (this.a11 != 0.0F ? Float.floatToIntBits(this.a11) : 0);
        i = 31 * i + (this.a12 != 0.0F ? Float.floatToIntBits(this.a12) : 0);
        i = 31 * i + (this.a20 != 0.0F ? Float.floatToIntBits(this.a20) : 0);
        i = 31 * i + (this.a21 != 0.0F ? Float.floatToIntBits(this.a21) : 0);
        i = 31 * i + (this.a22 != 0.0F ? Float.floatToIntBits(this.a22) : 0);
        return i;
    }

    private static int pack(int x, int y) {
        return y * 3 + x;
    }

    public void load(Matrix3f source) {
        this.a00 = source.a00;
        this.a01 = source.a01;
        this.a02 = source.a02;
        this.a10 = source.a10;
        this.a11 = source.a11;
        this.a12 = source.a12;
        this.a20 = source.a20;
        this.a21 = source.a21;
        this.a22 = source.a22;
    }

    /**
     * Writes this matrix to the buffer in column-major order.
     *
     * @see #writeRowMajor(FloatBuffer)
     * @see #write(FloatBuffer, boolean)
     */
    public void writeColumnMajor(FloatBuffer buf) {
        buf.put(Matrix3f.pack(0, 0), this.a00);
        buf.put(Matrix3f.pack(0, 1), this.a01);
        buf.put(Matrix3f.pack(0, 2), this.a02);
        buf.put(Matrix3f.pack(1, 0), this.a10);
        buf.put(Matrix3f.pack(1, 1), this.a11);
        buf.put(Matrix3f.pack(1, 2), this.a12);
        buf.put(Matrix3f.pack(2, 0), this.a20);
        buf.put(Matrix3f.pack(2, 1), this.a21);
        buf.put(Matrix3f.pack(2, 2), this.a22);
    }

    /**
     * Writes this matrix to the buffer in row-major order.
     *
     * @see #writeColumnMajor(FloatBuffer)
     * @see #write(FloatBuffer, boolean)
     */
    public void writeRowMajor(FloatBuffer buf) {
        buf.put(Matrix3f.pack(0, 0), this.a00);
        buf.put(Matrix3f.pack(1, 0), this.a01);
        buf.put(Matrix3f.pack(2, 0), this.a02);
        buf.put(Matrix3f.pack(0, 1), this.a10);
        buf.put(Matrix3f.pack(1, 1), this.a11);
        buf.put(Matrix3f.pack(2, 1), this.a12);
        buf.put(Matrix3f.pack(0, 2), this.a20);
        buf.put(Matrix3f.pack(1, 2), this.a21);
        buf.put(Matrix3f.pack(2, 2), this.a22);
    }

    /**
     * Writes this matrix to the buffer.
     *
     * @see #writeRowMajor(FloatBuffer)
     * @see #writeColumnMajor(FloatBuffer)
     *
     * @param rowMajor {@code true} to write in row-major order; {@code false} to write in
     * column-major order
     */
    public void write(FloatBuffer buf, boolean rowMajor) {
        if (rowMajor) {
            this.writeRowMajor(buf);
        } else {
            this.writeColumnMajor(buf);
        }
    }

    public String toString() {
        return "Matrix3f:\n" +
                this.a00 +
                " " +
                this.a01 +
                " " +
                this.a02 +
                "\n" +
                this.a10 +
                " " +
                this.a11 +
                " " +
                this.a12 +
                "\n" +
                this.a20 +
                " " +
                this.a21 +
                " " +
                this.a22 +
                "\n";
    }

    public void loadIdentity() {
        this.a00 = 1.0F;
        this.a01 = 0.0F;
        this.a02 = 0.0F;
        this.a10 = 0.0F;
        this.a11 = 1.0F;
        this.a12 = 0.0F;
        this.a20 = 0.0F;
        this.a21 = 0.0F;
        this.a22 = 1.0F;
    }

    public float determinantAndAdjugate() {
        float f = this.a11 * this.a22 - this.a12 * this.a21;
        float g = -(this.a10 * this.a22 - this.a12 * this.a20);
        float h = this.a10 * this.a21 - this.a11 * this.a20;
        float i = -(this.a01 * this.a22 - this.a02 * this.a21);
        float j = this.a00 * this.a22 - this.a02 * this.a20;
        float k = -(this.a00 * this.a21 - this.a01 * this.a20);
        float l = this.a01 * this.a12 - this.a02 * this.a11;
        float m = -(this.a00 * this.a12 - this.a02 * this.a10);
        float n = this.a00 * this.a11 - this.a01 * this.a10;
        float o = this.a00 * f + this.a01 * g + this.a02 * h;
        this.a00 = f;
        this.a10 = g;
        this.a20 = h;
        this.a01 = i;
        this.a11 = j;
        this.a21 = k;
        this.a02 = l;
        this.a12 = m;
        this.a22 = n;
        return o;
    }

    public boolean invert() {
        float f = this.determinantAndAdjugate();
        if (Math.abs(f) > 1.0E-6F) {
            this.multiply(f);
            return true;
        } else {
            return false;
        }
    }

    public void set(int x, int y, float value) {
        if (x == 0) {
            if (y == 0) {
                this.a00 = value;
            } else if (y == 1) {
                this.a01 = value;
            } else {
                this.a02 = value;
            }
        } else if (x == 1) {
            if (y == 0) {
                this.a10 = value;
            } else if (y == 1) {
                this.a11 = value;
            } else {
                this.a12 = value;
            }
        } else if (y == 0) {
            this.a20 = value;
        } else if (y == 1) {
            this.a21 = value;
        } else {
            this.a22 = value;
        }

    }

    public void multiply(Matrix3f other) {
        float f = this.a00 * other.a00 + this.a01 * other.a10 + this.a02 * other.a20;
        float g = this.a00 * other.a01 + this.a01 * other.a11 + this.a02 * other.a21;
        float h = this.a00 * other.a02 + this.a01 * other.a12 + this.a02 * other.a22;
        float i = this.a10 * other.a00 + this.a11 * other.a10 + this.a12 * other.a20;
        float j = this.a10 * other.a01 + this.a11 * other.a11 + this.a12 * other.a21;
        float k = this.a10 * other.a02 + this.a11 * other.a12 + this.a12 * other.a22;
        float l = this.a20 * other.a00 + this.a21 * other.a10 + this.a22 * other.a20;
        float m = this.a20 * other.a01 + this.a21 * other.a11 + this.a22 * other.a21;
        float n = this.a20 * other.a02 + this.a21 * other.a12 + this.a22 * other.a22;
        this.a00 = f;
        this.a01 = g;
        this.a02 = h;
        this.a10 = i;
        this.a11 = j;
        this.a12 = k;
        this.a20 = l;
        this.a21 = m;
        this.a22 = n;
    }

    public void multiply(Quaternion quaternion) {
        this.multiply(new Matrix3f(quaternion));
    }

    public void multiply(float scalar) {
        this.a00 *= scalar;
        this.a01 *= scalar;
        this.a02 *= scalar;
        this.a10 *= scalar;
        this.a11 *= scalar;
        this.a12 *= scalar;
        this.a20 *= scalar;
        this.a21 *= scalar;
        this.a22 *= scalar;
    }

    public Matrix3f copy() {
        return new Matrix3f(this);
    }
}
