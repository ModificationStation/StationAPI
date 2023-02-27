package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.math.AffineTransformation;
import net.modificationstation.stationapi.api.util.math.DirectionTransformation;
import net.modificationstation.stationapi.api.util.math.Quaternion;
import net.modificationstation.stationapi.api.util.math.Vec3f;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public enum ModelBakeRotation implements ModelBakeSettings {
   Y0_Z0(0, 0),
   Y0_Z90(0, 90),
   Y0_Z180(0, 180),
   Y0_Z270(0, 270),
   Y90_Z0(90, 0),
   Y90_Z90(90, 90),
   Y90_Z180(90, 180),
   Y90_Z270(90, 270),
   Y180_Z0(180, 0),
   Y180_Z90(180, 90),
   Y180_Z180(180, 180),
   Y180_Z270(180, 270),
   Y270_Z0(270, 0),
   Y270_Z90(270, 90),
   Y270_Z180(270, 180),
   Y270_Z270(270, 270);

   private static final Map<Integer, ModelBakeRotation> BY_INDEX = Arrays.stream(values()).collect(Collectors.toMap((modelRotation) -> modelRotation.index, (modelRotation) -> modelRotation));
   private final AffineTransformation rotation;
   private final DirectionTransformation directionTransformation;
   private final int index;

   private static int getIndex(int y, int z) {
      return y * 360 + z;
   }

   ModelBakeRotation(int y, int z) {
      this.index = getIndex(y, z);
      Quaternion quaternion = new Quaternion(new Vec3f(0.0F, 1.0F, 0.0F), (float)(-y), true);
      quaternion.hamiltonProduct(new Quaternion(new Vec3f(0.0F, 0.0F, 1.0F), (float)(-z), true));
      DirectionTransformation directionTransformation = DirectionTransformation.IDENTITY;

      int k;
      for(k = 0; k < y; k += 90) {
         directionTransformation = directionTransformation.prepend(DirectionTransformation.ROT_90_Y_NEG);
      }

      for(k = 0; k < z; k += 90) {
         directionTransformation = directionTransformation.prepend(DirectionTransformation.ROT_90_Z_NEG);
      }

      this.rotation = new AffineTransformation(null, quaternion, null, null);
      this.directionTransformation = directionTransformation;
   }

   public AffineTransformation getRotation() {
      return this.rotation;
   }

   public static ModelBakeRotation get(int y, int z) {
      return BY_INDEX.get(getIndex(Math.floorMod(y, 360), Math.floorMod(z, 360)));
   }
}
