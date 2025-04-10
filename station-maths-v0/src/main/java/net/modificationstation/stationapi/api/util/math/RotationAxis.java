package net.modificationstation.stationapi.api.util.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;

@FunctionalInterface
public interface RotationAxis {
    RotationAxis NEGATIVE_X = rad -> new Quaternionf().rotationX(-rad);
    RotationAxis POSITIVE_X = rad -> new Quaternionf().rotationX(rad);
    RotationAxis NEGATIVE_Y = rad -> new Quaternionf().rotationY(-rad);
    RotationAxis POSITIVE_Y = rad -> new Quaternionf().rotationY(rad);
    RotationAxis NEGATIVE_Z = rad -> new Quaternionf().rotationZ(-rad);
    RotationAxis POSITIVE_Z = rad -> new Quaternionf().rotationZ(rad);

    static RotationAxis of(Vector3f axis) {
        return rad -> new Quaternionf().rotationAxis(rad, axis);
    }

    Quaternionf rotation(float rad);

    default Quaternionf rotationDegrees(float deg) {
        return this.rotation(deg * (float) (Math.PI / 180.0));
    }
}
