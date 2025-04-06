package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public record ModelRotation(Vector3f origin, Direction.Axis axis, float angle, boolean rescale) { }
