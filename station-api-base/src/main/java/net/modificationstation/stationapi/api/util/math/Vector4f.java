package net.modificationstation.stationapi.api.util.math;

public class Vector4f {
   private float x;
   private float y;
   private float z;
   private float w;

   public Vector4f() {
   }

   public Vector4f(float x, float y, float z, float w) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
   }

   public Vector4f(Vec3f vector) {
      this(vector.getX(), vector.getY(), vector.getZ(), 1.0F);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Vector4f vector4f = (Vector4f)o;
         if (Float.compare(vector4f.x, this.x) != 0) {
            return false;
         } else if (Float.compare(vector4f.y, this.y) != 0) {
            return false;
         } else if (Float.compare(vector4f.z, this.z) != 0) {
            return false;
         } else {
            return Float.compare(vector4f.w, this.w) == 0;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int i = Float.floatToIntBits(this.x);
      i = 31 * i + Float.floatToIntBits(this.y);
      i = 31 * i + Float.floatToIntBits(this.z);
      i = 31 * i + Float.floatToIntBits(this.w);
      return i;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getZ() {
      return this.z;
   }

   public float getW() {
      return this.w;
   }

   public void multiplyComponentwise(Vec3f vector) {
      this.x *= vector.getX();
      this.y *= vector.getY();
      this.z *= vector.getZ();
   }

   public void set(float x, float y, float z, float w) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
   }

   public float dotProduct(Vector4f other) {
      return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
   }

   public boolean normalize() {
      float f = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
      if ((double)f < 1.0E-5D) {
         return false;
      } else {
         float g = MathHelper.fastInverseSqrt(f);
         this.x *= g;
         this.y *= g;
         this.z *= g;
         this.w *= g;
         return true;
      }
   }

   public void transform(Matrix4f matrix) {
      float f = this.x;
      float g = this.y;
      float h = this.z;
      float i = this.w;
      this.x = matrix.a00 * f + matrix.a01 * g + matrix.a02 * h + matrix.a03 * i;
      this.y = matrix.a10 * f + matrix.a11 * g + matrix.a12 * h + matrix.a13 * i;
      this.z = matrix.a20 * f + matrix.a21 * g + matrix.a22 * h + matrix.a23 * i;
      this.w = matrix.a30 * f + matrix.a31 * g + matrix.a32 * h + matrix.a33 * i;
   }

   public void rotate(Quaternion rotation) {
      Quaternion quaternion = new Quaternion(rotation);
      quaternion.hamiltonProduct(new Quaternion(this.getX(), this.getY(), this.getZ(), 0.0F));
      Quaternion quaternion2 = new Quaternion(rotation);
      quaternion2.conjugate();
      quaternion.hamiltonProduct(quaternion2);
      this.set(quaternion.getX(), quaternion.getY(), quaternion.getZ(), this.getW());
   }

   public void normalizeProjectiveCoordinates() {
      this.x /= this.w;
      this.y /= this.w;
      this.z /= this.w;
      this.w = 1.0F;
   }

   public String toString() {
      return "[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
   }
}
