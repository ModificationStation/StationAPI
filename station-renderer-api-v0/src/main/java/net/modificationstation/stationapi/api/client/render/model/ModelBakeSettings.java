package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.math.AffineTransformation;

@Environment(EnvType.CLIENT)
public interface ModelBakeSettings {
   default AffineTransformation getRotation() {
      return AffineTransformation.identity();
   }

   default boolean uvlock() {
      return false;
   }
}
