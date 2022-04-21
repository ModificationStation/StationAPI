package net.modificationstation.stationapi.api.util.math;

import java.nio.FloatBuffer;

public final class Matrix4f {
    float a00;
    float a01;
    float a02;
    float a03;
    float a10;
    float a11;
    float a12;
    float a13;
    float a20;
    float a21;
    float a22;
    float a23;
    float a30;
    float a31;
    float a32;
    float a33;

    public Matrix4f() {
    }

    public Matrix4f(Matrix4f source) {
        this.a00 = source.a00;
        this.a01 = source.a01;
        this.a02 = source.a02;
        this.a03 = source.a03;
        this.a10 = source.a10;
        this.a11 = source.a11;
        this.a12 = source.a12;
        this.a13 = source.a13;
        this.a20 = source.a20;
        this.a21 = source.a21;
        this.a22 = source.a22;
        this.a23 = source.a23;
        this.a30 = source.a30;
        this.a31 = source.a31;
        this.a32 = source.a32;
        this.a33 = source.a33;
    }

    public Matrix4f(Quaternion quaternion) {
        float f = quaternion.getX();
        float g = quaternion.getY();
        float h = quaternion.getZ();
        float i = quaternion.getW();
        float j = 2.0F * f * f;
        float k = 2.0F * g * g;
        float l = 2.0F * h * h;
        this.a00 = 1.0F - k - l;
        this.a11 = 1.0F - l - j;
        this.a22 = 1.0F - j - k;
        this.a33 = 1.0F;
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Matrix4f matrix4f = (Matrix4f)o;
            return Float.compare(matrix4f.a00, this.a00) == 0 && Float.compare(matrix4f.a01, this.a01) == 0 && Float.compare(matrix4f.a02, this.a02) == 0 && Float.compare(matrix4f.a03, this.a03) == 0 && Float.compare(matrix4f.a10, this.a10) == 0 && Float.compare(matrix4f.a11, this.a11) == 0 && Float.compare(matrix4f.a12, this.a12) == 0 && Float.compare(matrix4f.a13, this.a13) == 0 && Float.compare(matrix4f.a20, this.a20) == 0 && Float.compare(matrix4f.a21, this.a21) == 0 && Float.compare(matrix4f.a22, this.a22) == 0 && Float.compare(matrix4f.a23, this.a23) == 0 && Float.compare(matrix4f.a30, this.a30) == 0 && Float.compare(matrix4f.a31, this.a31) == 0 && Float.compare(matrix4f.a32, this.a32) == 0 && Float.compare(matrix4f.a33, this.a33) == 0;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = this.a00 != 0.0F ? Float.floatToIntBits(this.a00) : 0;
        i = 31 * i + (this.a01 != 0.0F ? Float.floatToIntBits(this.a01) : 0);
        i = 31 * i + (this.a02 != 0.0F ? Float.floatToIntBits(this.a02) : 0);
        i = 31 * i + (this.a03 != 0.0F ? Float.floatToIntBits(this.a03) : 0);
        i = 31 * i + (this.a10 != 0.0F ? Float.floatToIntBits(this.a10) : 0);
        i = 31 * i + (this.a11 != 0.0F ? Float.floatToIntBits(this.a11) : 0);
        i = 31 * i + (this.a12 != 0.0F ? Float.floatToIntBits(this.a12) : 0);
        i = 31 * i + (this.a13 != 0.0F ? Float.floatToIntBits(this.a13) : 0);
        i = 31 * i + (this.a20 != 0.0F ? Float.floatToIntBits(this.a20) : 0);
        i = 31 * i + (this.a21 != 0.0F ? Float.floatToIntBits(this.a21) : 0);
        i = 31 * i + (this.a22 != 0.0F ? Float.floatToIntBits(this.a22) : 0);
        i = 31 * i + (this.a23 != 0.0F ? Float.floatToIntBits(this.a23) : 0);
        i = 31 * i + (this.a30 != 0.0F ? Float.floatToIntBits(this.a30) : 0);
        i = 31 * i + (this.a31 != 0.0F ? Float.floatToIntBits(this.a31) : 0);
        i = 31 * i + (this.a32 != 0.0F ? Float.floatToIntBits(this.a32) : 0);
        i = 31 * i + (this.a33 != 0.0F ? Float.floatToIntBits(this.a33) : 0);
        return i;
    }

    public void load(Matrix4f source) {
        this.a00 = source.a00;
        this.a01 = source.a01;
        this.a02 = source.a02;
        this.a03 = source.a03;
        this.a10 = source.a10;
        this.a11 = source.a11;
        this.a12 = source.a12;
        this.a13 = source.a13;
        this.a20 = source.a20;
        this.a21 = source.a21;
        this.a22 = source.a22;
        this.a23 = source.a23;
        this.a30 = source.a30;
        this.a31 = source.a31;
        this.a32 = source.a32;
        this.a33 = source.a33;
    }

    /**
     * Writes this matrix to the buffer in column-major order.
     *
     * @see #writeRowMajor(FloatBuffer)
     * @see #write(FloatBuffer, boolean)
     */
    public void writeColumnMajor(FloatBuffer buf) {
        buf.put(Matrix4f.pack(0, 0), this.a00);
        buf.put(Matrix4f.pack(0, 1), this.a01);
        buf.put(Matrix4f.pack(0, 2), this.a02);
        buf.put(Matrix4f.pack(0, 3), this.a03);
        buf.put(Matrix4f.pack(1, 0), this.a10);
        buf.put(Matrix4f.pack(1, 1), this.a11);
        buf.put(Matrix4f.pack(1, 2), this.a12);
        buf.put(Matrix4f.pack(1, 3), this.a13);
        buf.put(Matrix4f.pack(2, 0), this.a20);
        buf.put(Matrix4f.pack(2, 1), this.a21);
        buf.put(Matrix4f.pack(2, 2), this.a22);
        buf.put(Matrix4f.pack(2, 3), this.a23);
        buf.put(Matrix4f.pack(3, 0), this.a30);
        buf.put(Matrix4f.pack(3, 1), this.a31);
        buf.put(Matrix4f.pack(3, 2), this.a32);
        buf.put(Matrix4f.pack(3, 3), this.a33);
    }

    /**
     * Writes this matrix to the buffer in row-major order.
     *
     * @see #writeColumnMajor(FloatBuffer)
     * @see #write(FloatBuffer, boolean)
     */
    public void writeRowMajor(FloatBuffer buf) {
        buf.put(Matrix4f.pack(0, 0), this.a00);
        buf.put(Matrix4f.pack(1, 0), this.a01);
        buf.put(Matrix4f.pack(2, 0), this.a02);
        buf.put(Matrix4f.pack(3, 0), this.a03);
        buf.put(Matrix4f.pack(0, 1), this.a10);
        buf.put(Matrix4f.pack(1, 1), this.a11);
        buf.put(Matrix4f.pack(2, 1), this.a12);
        buf.put(Matrix4f.pack(3, 1), this.a13);
        buf.put(Matrix4f.pack(0, 2), this.a20);
        buf.put(Matrix4f.pack(1, 2), this.a21);
        buf.put(Matrix4f.pack(2, 2), this.a22);
        buf.put(Matrix4f.pack(3, 2), this.a23);
        buf.put(Matrix4f.pack(0, 3), this.a30);
        buf.put(Matrix4f.pack(1, 3), this.a31);
        buf.put(Matrix4f.pack(2, 3), this.a32);
        buf.put(Matrix4f.pack(3, 3), this.a33);
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

    private static int pack(int x, int y) {
        return y * 4 + x;
    }

    public String toString() {
        return "Matrix4f:\n" +
                this.a00 +
                " " +
                this.a01 +
                " " +
                this.a02 +
                " " +
                this.a03 +
                "\n" +
                this.a10 +
                " " +
                this.a11 +
                " " +
                this.a12 +
                " " +
                this.a13 +
                "\n" +
                this.a20 +
                " " +
                this.a21 +
                " " +
                this.a22 +
                " " +
                this.a23 +
                "\n" +
                this.a30 +
                " " +
                this.a31 +
                " " +
                this.a32 +
                " " +
                this.a33 +
                "\n";
    }

    public void writeToBuffer(FloatBuffer floatBuffer) {
        floatBuffer.put(pack(0, 0), this.a00);
        floatBuffer.put(pack(0, 1), this.a01);
        floatBuffer.put(pack(0, 2), this.a02);
        floatBuffer.put(pack(0, 3), this.a03);
        floatBuffer.put(pack(1, 0), this.a10);
        floatBuffer.put(pack(1, 1), this.a11);
        floatBuffer.put(pack(1, 2), this.a12);
        floatBuffer.put(pack(1, 3), this.a13);
        floatBuffer.put(pack(2, 0), this.a20);
        floatBuffer.put(pack(2, 1), this.a21);
        floatBuffer.put(pack(2, 2), this.a22);
        floatBuffer.put(pack(2, 3), this.a23);
        floatBuffer.put(pack(3, 0), this.a30);
        floatBuffer.put(pack(3, 1), this.a31);
        floatBuffer.put(pack(3, 2), this.a32);
        floatBuffer.put(pack(3, 3), this.a33);
    }

    public void loadIdentity() {
        this.a00 = 1.0F;
        this.a01 = 0.0F;
        this.a02 = 0.0F;
        this.a03 = 0.0F;
        this.a10 = 0.0F;
        this.a11 = 1.0F;
        this.a12 = 0.0F;
        this.a13 = 0.0F;
        this.a20 = 0.0F;
        this.a21 = 0.0F;
        this.a22 = 1.0F;
        this.a23 = 0.0F;
        this.a30 = 0.0F;
        this.a31 = 0.0F;
        this.a32 = 0.0F;
        this.a33 = 1.0F;
    }

    public float determinantAndAdjugate() {
        float f = this.a00 * this.a11 - this.a01 * this.a10;
        float g = this.a00 * this.a12 - this.a02 * this.a10;
        float h = this.a00 * this.a13 - this.a03 * this.a10;
        float i = this.a01 * this.a12 - this.a02 * this.a11;
        float j = this.a01 * this.a13 - this.a03 * this.a11;
        float k = this.a02 * this.a13 - this.a03 * this.a12;
        float l = this.a20 * this.a31 - this.a21 * this.a30;
        float m = this.a20 * this.a32 - this.a22 * this.a30;
        float n = this.a20 * this.a33 - this.a23 * this.a30;
        float o = this.a21 * this.a32 - this.a22 * this.a31;
        float p = this.a21 * this.a33 - this.a23 * this.a31;
        float q = this.a22 * this.a33 - this.a23 * this.a32;
        float r = this.a11 * q - this.a12 * p + this.a13 * o;
        float s = -this.a10 * q + this.a12 * n - this.a13 * m;
        float t = this.a10 * p - this.a11 * n + this.a13 * l;
        float u = -this.a10 * o + this.a11 * m - this.a12 * l;
        float v = -this.a01 * q + this.a02 * p - this.a03 * o;
        float w = this.a00 * q - this.a02 * n + this.a03 * m;
        float x = -this.a00 * p + this.a01 * n - this.a03 * l;
        float y = this.a00 * o - this.a01 * m + this.a02 * l;
        float z = this.a31 * k - this.a32 * j + this.a33 * i;
        float aa = -this.a30 * k + this.a32 * h - this.a33 * g;
        float ab = this.a30 * j - this.a31 * h + this.a33 * f;
        float ac = -this.a30 * i + this.a31 * g - this.a32 * f;
        float ad = -this.a21 * k + this.a22 * j - this.a23 * i;
        float ae = this.a20 * k - this.a22 * h + this.a23 * g;
        float af = -this.a20 * j + this.a21 * h - this.a23 * f;
        float ag = this.a20 * i - this.a21 * g + this.a22 * f;
        this.a00 = r;
        this.a10 = s;
        this.a20 = t;
        this.a30 = u;
        this.a01 = v;
        this.a11 = w;
        this.a21 = x;
        this.a31 = y;
        this.a02 = z;
        this.a12 = aa;
        this.a22 = ab;
        this.a32 = ac;
        this.a03 = ad;
        this.a13 = ae;
        this.a23 = af;
        this.a33 = ag;
        return f * q - g * p + h * o + i * n - j * m + k * l;
    }

    public void transpose() {
        float f = this.a10;
        this.a10 = this.a01;
        this.a01 = f;
        f = this.a20;
        this.a20 = this.a02;
        this.a02 = f;
        f = this.a21;
        this.a21 = this.a12;
        this.a12 = f;
        f = this.a30;
        this.a30 = this.a03;
        this.a03 = f;
        f = this.a31;
        this.a31 = this.a13;
        this.a13 = f;
        f = this.a32;
        this.a32 = this.a23;
        this.a23 = f;
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

    public void multiply(Matrix4f matrix) {
        float f = this.a00 * matrix.a00 + this.a01 * matrix.a10 + this.a02 * matrix.a20 + this.a03 * matrix.a30;
        float g = this.a00 * matrix.a01 + this.a01 * matrix.a11 + this.a02 * matrix.a21 + this.a03 * matrix.a31;
        float h = this.a00 * matrix.a02 + this.a01 * matrix.a12 + this.a02 * matrix.a22 + this.a03 * matrix.a32;
        float i = this.a00 * matrix.a03 + this.a01 * matrix.a13 + this.a02 * matrix.a23 + this.a03 * matrix.a33;
        float j = this.a10 * matrix.a00 + this.a11 * matrix.a10 + this.a12 * matrix.a20 + this.a13 * matrix.a30;
        float k = this.a10 * matrix.a01 + this.a11 * matrix.a11 + this.a12 * matrix.a21 + this.a13 * matrix.a31;
        float l = this.a10 * matrix.a02 + this.a11 * matrix.a12 + this.a12 * matrix.a22 + this.a13 * matrix.a32;
        float m = this.a10 * matrix.a03 + this.a11 * matrix.a13 + this.a12 * matrix.a23 + this.a13 * matrix.a33;
        float n = this.a20 * matrix.a00 + this.a21 * matrix.a10 + this.a22 * matrix.a20 + this.a23 * matrix.a30;
        float o = this.a20 * matrix.a01 + this.a21 * matrix.a11 + this.a22 * matrix.a21 + this.a23 * matrix.a31;
        float p = this.a20 * matrix.a02 + this.a21 * matrix.a12 + this.a22 * matrix.a22 + this.a23 * matrix.a32;
        float q = this.a20 * matrix.a03 + this.a21 * matrix.a13 + this.a22 * matrix.a23 + this.a23 * matrix.a33;
        float r = this.a30 * matrix.a00 + this.a31 * matrix.a10 + this.a32 * matrix.a20 + this.a33 * matrix.a30;
        float s = this.a30 * matrix.a01 + this.a31 * matrix.a11 + this.a32 * matrix.a21 + this.a33 * matrix.a31;
        float t = this.a30 * matrix.a02 + this.a31 * matrix.a12 + this.a32 * matrix.a22 + this.a33 * matrix.a32;
        float u = this.a30 * matrix.a03 + this.a31 * matrix.a13 + this.a32 * matrix.a23 + this.a33 * matrix.a33;
        this.a00 = f;
        this.a01 = g;
        this.a02 = h;
        this.a03 = i;
        this.a10 = j;
        this.a11 = k;
        this.a12 = l;
        this.a13 = m;
        this.a20 = n;
        this.a21 = o;
        this.a22 = p;
        this.a23 = q;
        this.a30 = r;
        this.a31 = s;
        this.a32 = t;
        this.a33 = u;
    }

    public void multiply(Quaternion quaternion) {
        this.multiply(new Matrix4f(quaternion));
    }

    public void multiply(float scalar) {
        this.a00 *= scalar;
        this.a01 *= scalar;
        this.a02 *= scalar;
        this.a03 *= scalar;
        this.a10 *= scalar;
        this.a11 *= scalar;
        this.a12 *= scalar;
        this.a13 *= scalar;
        this.a20 *= scalar;
        this.a21 *= scalar;
        this.a22 *= scalar;
        this.a23 *= scalar;
        this.a30 *= scalar;
        this.a31 *= scalar;
        this.a32 *= scalar;
        this.a33 *= scalar;
    }

    public static Matrix4f viewboxMatrix(double fov, float aspectRatio, float cameraDepth, float viewDistance) {
        float f = (float)(1.0D / Math.tan(fov * 0.01745329238474369D / 2.0D));
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.a00 = f / aspectRatio;
        matrix4f.a11 = f;
        matrix4f.a22 = (viewDistance + cameraDepth) / (cameraDepth - viewDistance);
        matrix4f.a32 = -1.0F;
        matrix4f.a23 = 2.0F * viewDistance * cameraDepth / (cameraDepth - viewDistance);
        return matrix4f;
    }

    public static Matrix4f projectionMatrix(float width, float height, float nearPlane, float farPlane) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.a00 = 2.0F / width;
        matrix4f.a11 = 2.0F / height;
        float f = farPlane - nearPlane;
        matrix4f.a22 = -2.0F / f;
        matrix4f.a33 = 1.0F;
        matrix4f.a03 = -1.0F;
        matrix4f.a13 = -1.0F;
        matrix4f.a23 = -(farPlane + nearPlane) / f;
        return matrix4f;
    }

    public static Matrix4f projectionMatrix(float left, float right, float bottom, float top, float nearPlane, float farPlane) {
        Matrix4f matrix4f = new Matrix4f();
        float f = right - left;
        float g = bottom - top;
        float h = farPlane - nearPlane;
        matrix4f.a00 = 2.0f / f;
        matrix4f.a11 = 2.0f / g;
        matrix4f.a22 = -2.0f / h;
        matrix4f.a03 = -(right + left) / f;
        matrix4f.a13 = -(bottom + top) / g;
        matrix4f.a23 = -(farPlane + nearPlane) / h;
        matrix4f.a33 = 1.0f;
        return matrix4f;
    }

    public void addToLastColumn(Vec3f vector) {
        this.a03 += vector.getX();
        this.a13 += vector.getY();
        this.a23 += vector.getZ();
    }

    public Matrix4f copy() {
        return new Matrix4f(this);
    }

    public static Matrix4f scale(float x, float y, float z) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.a00 = x;
        matrix4f.a11 = y;
        matrix4f.a22 = z;
        matrix4f.a33 = 1.0F;
        return matrix4f;
    }

    public static Matrix4f scaleTmp(float x, float y, float z) {
        tmpMatrix4f.loadIdentity();
        tmpMatrix4f.a00 = x;
        tmpMatrix4f.a11 = y;
        tmpMatrix4f.a22 = z;
        return tmpMatrix4f;
    }

    public static Matrix4f translate(float x, float y, float z) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.a00 = 1.0F;
        matrix4f.a11 = 1.0F;
        matrix4f.a22 = 1.0F;
        matrix4f.a33 = 1.0F;
        matrix4f.a03 = x;
        matrix4f.a13 = y;
        matrix4f.a23 = z;
        return matrix4f;
    }

    public static Matrix4f translateTmp(float x, float y, float z) {
        tmpMatrix4f.loadIdentity();
        tmpMatrix4f.a03 = x;
        tmpMatrix4f.a13 = y;
        tmpMatrix4f.a23 = z;
        return tmpMatrix4f;
    }

    private static final Matrix4f tmpMatrix4f = new Matrix4f();
}
