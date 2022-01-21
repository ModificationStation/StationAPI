package net.modificationstation.stationapi.api.client.model.json;

import lombok.RequiredArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.util.math.Axis;

@Environment(EnvType.CLIENT)
@RequiredArgsConstructor
public class ModelRotation {

   public final Vec3f origin;
   public final Axis axis;
   public final float angle;
   public final boolean rescale;
}
