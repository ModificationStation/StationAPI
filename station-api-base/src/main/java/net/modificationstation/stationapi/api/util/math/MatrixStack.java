package net.modificationstation.stationapi.api.util.math;

import net.modificationstation.stationapi.api.util.Util;

import java.util.*;

public class MatrixStack {
   private final Deque<MatrixStack.Entry> stack = Util.make(new ArrayDeque<>(), (arrayDeque) -> {
      Matrix4f matrix4f = new Matrix4f();
      matrix4f.loadIdentity();
      Matrix3f matrix3f = new Matrix3f();
      matrix3f.loadIdentity();
      arrayDeque.add(new Entry(matrix4f, matrix3f));
   });

   public void translate(double x, double y, double z) {
      MatrixStack.Entry entry = this.stack.getLast();
      entry.modelMatrix.multiply(Matrix4f.translate((float)x, (float)y, (float)z));
   }

   public void scale(float x, float y, float z) {
      MatrixStack.Entry entry = this.stack.getLast();
      entry.modelMatrix.multiply(Matrix4f.scale(x, y, z));
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
      entry.normalMatrix.multiply(Matrix3f.scale(i * f, i * g, i * h));
   }

   public void multiply(Quaternion quaternion) {
      MatrixStack.Entry entry = this.stack.getLast();
      entry.modelMatrix.multiply(quaternion);
      entry.normalMatrix.multiply(quaternion);
   }

   public void push() {
      MatrixStack.Entry entry = this.stack.getLast();
      this.stack.addLast(new MatrixStack.Entry(entry.modelMatrix.copy(), entry.normalMatrix.copy()));
   }

   public void pop() {
      this.stack.removeLast();
   }

   public MatrixStack.Entry peek() {
      return this.stack.getLast();
   }

   public boolean isEmpty() {
      return this.stack.size() == 1;
   }

   public static final class Entry {
      private final Matrix4f modelMatrix;
      private final Matrix3f normalMatrix;

      private Entry(Matrix4f matrix4f, Matrix3f matrix3f) {
         this.modelMatrix = matrix4f;
         this.normalMatrix = matrix3f;
      }

      public Matrix4f getModel() {
         return this.modelMatrix;
      }

      public Matrix3f getNormal() {
         return this.normalMatrix;
      }
   }
}
