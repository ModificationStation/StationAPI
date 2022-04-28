package net.modificationstation.stationapi.api.util.math;

import net.modificationstation.stationapi.api.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MatrixStack {

    private final List<MatrixStack.Entry> stack = Util.make(new ArrayList<>(), list -> list.add(new Entry(Util.make(new Matrix4f(), Matrix4f::loadIdentity), Util.make(new Matrix3f(), Matrix3f::loadIdentity))));
    private int n = 0;

    public void translate(double x, double y, double z) {
        MatrixStack.Entry entry = this.stack.get(n);
        entry.positionMatrix.multiply(Matrix4f.translateTmp((float)x, (float)y, (float)z));
    }

    public void scale(float x, float y, float z) {
        MatrixStack.Entry entry = this.stack.get(n);
        entry.positionMatrix.multiply(Matrix4f.scaleTmp(x, y, z));
        if (x == y && y == z) {
            if (x > 0.0F) {
                return;
            }

            entry.normalMatrix.multiply(-1.0F);
        }

        float f = 1.0F / x;
        float g = 1.0F / y;
        float h = 1.0F / z;
        float i = MathHelper.fastInverseCbrt(f * g * h);
        entry.normalMatrix.multiply(Matrix3f.scaleTmp(i * f, i * g, i * h));
    }

    public void multiply(Quaternion quaternion) {
        MatrixStack.Entry entry = this.stack.get(n);
        entry.positionMatrix.multiply(quaternion);
        entry.normalMatrix.multiply(quaternion);
    }

    public void push() {
        MatrixStack.Entry entry = this.stack.get(n);
        n++;
        if (n >= stack.size()) {
            stack.add(new Entry(Util.make(new Matrix4f(), Matrix4f::loadIdentity), Util.make(new Matrix3f(), Matrix3f::loadIdentity)));
        }
        Entry nextEntry = stack.get(n);
        nextEntry.positionMatrix.load(entry.positionMatrix);
        nextEntry.normalMatrix.load(entry.normalMatrix);
    }

    public void pop() {
        n--;
    }

    public MatrixStack.Entry peek() {
        return this.stack.get(n);
    }

    public boolean isEmpty() {
        return this.stack.size() == 1;
    }

    public void loadIdentity() {
        Entry entry = this.stack.get(n);
        entry.positionMatrix.loadIdentity();
        entry.normalMatrix.loadIdentity();
    }

    public void multiplyPositionMatrix(Matrix4f matrix) {
        this.stack.get(n).positionMatrix.multiply(matrix);
    }

    public static final class Entry {
        private final Matrix4f positionMatrix;
        private final Matrix3f normalMatrix;

        private Entry(Matrix4f matrix4f, Matrix3f matrix3f) {
            this.positionMatrix = matrix4f;
            this.normalMatrix = matrix3f;
        }

        public Matrix4f getPositionMatrix() {
            return this.positionMatrix;
        }

        public Matrix3f getNormalMatrix() {
            return this.normalMatrix;
        }
    }
}
