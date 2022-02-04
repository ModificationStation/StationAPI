package net.modificationstation.stationapi.api.client.render.model;

import lombok.RequiredArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vector3f;

@Environment(EnvType.CLIENT)
@RequiredArgsConstructor
public class ModelRotation {

   public final Vector3f origin;
   public final Direction.Axis axis;
   public final float angle;
   public final boolean rescale;
}
