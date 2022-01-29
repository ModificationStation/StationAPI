package net.modificationstation.stationapi.api.util.math;

import net.modificationstation.stationapi.api.util.Util;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

import java.util.*;

public final class AffineTransformation {
   private final Matrix4f matrix;
   private boolean initialized;
   @Nullable
   private Vector3f translation;
   @Nullable
   private Quaternion rotation2;
   @Nullable
   private Vector3f scale;
   @Nullable
   private Quaternion rotation1;
   private static final AffineTransformation IDENTITY = Util.make(() -> {
      Matrix4f matrix4f = new Matrix4f();
      matrix4f.loadIdentity();
      AffineTransformation affineTransformation = new AffineTransformation(matrix4f);
      affineTransformation.getRotation2();
      return affineTransformation;
   });

   public AffineTransformation(@Nullable Matrix4f matrix) {
      if (matrix == null) {
         this.matrix = IDENTITY.matrix;
      } else {
         this.matrix = matrix;
      }

   }

   public AffineTransformation(@Nullable Vector3f translation, @Nullable Quaternion rotation2, @Nullable Vector3f scale, @Nullable Quaternion rotation1) {
      this.matrix = setup(translation, rotation2, scale, rotation1);
      this.translation = translation != null ? translation : new Vector3f();
      this.rotation2 = rotation2 != null ? rotation2 : Quaternion.IDENTITY.copy();
      this.scale = scale != null ? scale : new Vector3f(1.0F, 1.0F, 1.0F);
      this.rotation1 = rotation1 != null ? rotation1 : Quaternion.IDENTITY.copy();
      this.initialized = true;
   }

   public static AffineTransformation identity() {
      return IDENTITY;
   }

   public AffineTransformation multiply(AffineTransformation other) {
      Matrix4f matrix4f = this.getMatrix();
      matrix4f.multiply(other.getMatrix());
      return new AffineTransformation(matrix4f);
   }

   @Nullable
   public AffineTransformation invert() {
      if (this == IDENTITY) {
         return this;
      } else {
         Matrix4f matrix4f = this.getMatrix();
         return matrix4f.invert() ? new AffineTransformation(matrix4f) : null;
      }
   }

   private void init() {
      if (!this.initialized) {
         BiTuple<Matrix3f, Vector3f> pair = getLinearTransformationAndTranslationFromAffine(this.matrix);
         Triple<Quaternion, Vector3f, Quaternion> triple = ((Matrix3f)pair.one()).decomposeLinearTransformation();
         this.translation = (Vector3f)pair.two();
         this.rotation2 = (Quaternion)triple.getLeft();
         this.scale = (Vector3f)triple.getMiddle();
         this.rotation1 = (Quaternion)triple.getRight();
         this.initialized = true;
      }

   }

   private static Matrix4f setup(@Nullable Vector3f translation, @Nullable Quaternion rotation2, @Nullable Vector3f scale, @Nullable Quaternion rotation1) {
      Matrix4f matrix4f = new Matrix4f();
      matrix4f.loadIdentity();
      if (rotation2 != null) {
         matrix4f.multiply(new Matrix4f(rotation2));
      }

      if (scale != null) {
         matrix4f.multiply(Matrix4f.scale(scale.getX(), scale.getY(), scale.getZ()));
      }

      if (rotation1 != null) {
         matrix4f.multiply(new Matrix4f(rotation1));
      }

      if (translation != null) {
         matrix4f.a03 = translation.getX();
         matrix4f.a13 = translation.getY();
         matrix4f.a23 = translation.getZ();
      }

      return matrix4f;
   }

   public static BiTuple<Matrix3f, Vector3f> getLinearTransformationAndTranslationFromAffine(Matrix4f affineTransform) {
      affineTransform.multiply(1.0F / affineTransform.a33);
      Vector3f vector3f = new Vector3f(affineTransform.a03, affineTransform.a13, affineTransform.a23);
      Matrix3f matrix3f = new Matrix3f(affineTransform);
      return Tuple.tuple(matrix3f, vector3f);
   }

   public Matrix4f getMatrix() {
      return this.matrix.copy();
   }

   public Quaternion getRotation2() {
      this.init();
      return this.rotation2.copy();
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
}
