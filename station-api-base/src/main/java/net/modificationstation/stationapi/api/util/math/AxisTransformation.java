package net.modificationstation.stationapi.api.util.math;

import net.modificationstation.stationapi.api.util.Util;

import java.util.*;

public enum AxisTransformation {
   P123(0, 1, 2),
   P213(1, 0, 2),
   P132(0, 2, 1),
   P231(1, 2, 0),
   P312(2, 0, 1),
   P321(2, 1, 0);

   private final int[] mappings;
   private final Matrix3f matrix;
   private static final AxisTransformation[][] COMBINATIONS = Util.make(new AxisTransformation[values().length][values().length], (axisTransformations) -> {
      AxisTransformation[] var1 = values();
      for (AxisTransformation axisTransformation : var1) {
         AxisTransformation[] var5 = values();
         for (AxisTransformation axisTransformation2 : var5) {
            int[] is = new int[3];

            for (int i = 0; i < 3; ++i) {
               is[i] = axisTransformation.mappings[axisTransformation2.mappings[i]];
            }

            AxisTransformation axisTransformation3 = Arrays.stream(values()).filter((axisTransformationx) -> Arrays.equals(axisTransformationx.mappings, is)).findFirst().orElseThrow(NullPointerException::new);
            axisTransformations[axisTransformation.ordinal()][axisTransformation2.ordinal()] = axisTransformation3;
         }
      }

   });

   AxisTransformation(int xMapping, int yMapping, int zMapping) {
      this.mappings = new int[]{xMapping, yMapping, zMapping};
      this.matrix = new Matrix3f();
      this.matrix.set(0, this.map(0), 1.0F);
      this.matrix.set(1, this.map(1), 1.0F);
      this.matrix.set(2, this.map(2), 1.0F);
   }

   public AxisTransformation prepend(AxisTransformation transformation) {
      return COMBINATIONS[this.ordinal()][transformation.ordinal()];
   }

   public int map(int oldAxis) {
      return this.mappings[oldAxis];
   }

   public Matrix3f getMatrix() {
      return this.matrix;
   }
}
