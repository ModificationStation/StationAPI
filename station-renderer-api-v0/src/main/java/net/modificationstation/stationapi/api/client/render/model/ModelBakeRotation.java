package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.math.AffineTransformation;
import net.modificationstation.stationapi.api.util.math.DirectionTransformation;
import net.modificationstation.stationapi.api.util.math.Quaternion;
import net.modificationstation.stationapi.api.util.math.Vector3f;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public enum ModelBakeRotation implements ModelBakeSettings {
   X0_Y0(0, 0),
   X0_Y90(0, 90),
   X0_Y180(0, 180),
   X0_Y270(0, 270),
   X90_Y0(90, 0),
   X90_Y90(90, 90),
   X90_Y180(90, 180),
   X90_Y270(90, 270),
   X180_Y0(180, 0),
   X180_Y90(180, 90),
   X180_Y180(180, 180),
   X180_Y270(180, 270),
   X270_Y0(270, 0),
   X270_Y90(270, 90),
   X270_Y180(270, 180),
   X270_Y270(270, 270);

   private static final Map<Integer, ModelBakeRotation> BY_INDEX = Arrays.stream(values()).collect(Collectors.toMap((modelRotation) -> modelRotation.index, (modelRotation) -> modelRotation));
   private final AffineTransformation rotation;
   private final DirectionTransformation directionTransformation;
   private final int index;

   private static int getIndex(int x, int y) {
      return x * 360 + y;
   }

   ModelBakeRotation(int x, int y) {
      this.index = getIndex(x, y);
      Quaternion quaternion = new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), (float)(-y), true);
      quaternion.hamiltonProduct(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), (float)(-x), true));
      DirectionTransformation directionTransformation = DirectionTransformation.IDENTITY;

      int k;
      for(k = 0; k < y; k += 90) {
         directionTransformation = directionTransformation.prepend(DirectionTransformation.ROT_90_Y_NEG);
      }

      for(k = 0; k < x; k += 90) {
         directionTransformation = directionTransformation.prepend(DirectionTransformation.ROT_90_X_NEG);
      }

      this.rotation = new AffineTransformation(null, quaternion, null, null);
      this.directionTransformation = directionTransformation;
   }

   public AffineTransformation getRotation() {
      return this.rotation;
   }

   public static ModelBakeRotation get(int x, int y) {
      return BY_INDEX.get(getIndex(Math.floorMod(x, 360), Math.floorMod(y, 360)));
   }
}
