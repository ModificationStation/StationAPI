package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public record ModelRotation(Vec3f origin, Direction.Axis axis, float angle, boolean rescale) { }
