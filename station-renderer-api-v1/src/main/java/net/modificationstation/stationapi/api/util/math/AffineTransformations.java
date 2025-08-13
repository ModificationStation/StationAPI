package net.modificationstation.stationapi.api.util.math;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.EnumMap;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class AffineTransformations {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final EnumMap<Direction, AffineTransformation> DIRECTION_ROTATIONS = Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
        enumMap.put(Direction.WEST, AffineTransformation.identity());
        enumMap.put(Direction.SOUTH, new AffineTransformation(null, new Quaternionf().rotateY((float) (Math.PI / 2)), null, null));
        enumMap.put(Direction.NORTH, new AffineTransformation(null, new Quaternionf().rotateY((float) (-Math.PI / 2)), null, null));
        enumMap.put(Direction.EAST, new AffineTransformation(null, new Quaternionf().rotateY((float) Math.PI), null, null));
        enumMap.put(Direction.UP, new AffineTransformation(null, new Quaternionf().rotateX((float) (-Math.PI / 2)), null, null));
        enumMap.put(Direction.DOWN, new AffineTransformation(null, new Quaternionf().rotateX((float) (Math.PI / 2)), null, null));
    });
    public static final EnumMap<Direction, AffineTransformation> INVERTED_DIRECTION_ROTATIONS = Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
        Direction[] var1 = Direction.values();

        for (Direction direction : var1) {
            enumMap.put(direction, DIRECTION_ROTATIONS.get(direction).invert());
        }

    });

    public static AffineTransformation setupUvLock(AffineTransformation affineTransformation) {
        Matrix4f matrix4f = new Matrix4f().translation(0.5F, 0.5F, 0.5F);
        matrix4f.mul(affineTransformation.getMatrix());
        matrix4f.translate(-0.5F, -0.5F, -0.5F);
        return new AffineTransformation(matrix4f);
    }

    public static AffineTransformation uvLock(AffineTransformation affineTransformation, Direction direction, Supplier<String> supplier) {
        Direction direction2 = Direction.transform(affineTransformation.getMatrix(), direction);
        AffineTransformation affineTransformation2 = affineTransformation.invert();
        if (affineTransformation2 == null) {
            LOGGER.warn(supplier.get());
            return new AffineTransformation(null, null, new Vector3f(0.0F, 0.0F, 0.0F), null);
        } else {
            AffineTransformation affineTransformation3 = INVERTED_DIRECTION_ROTATIONS.get(direction).multiply(affineTransformation2).multiply(DIRECTION_ROTATIONS.get(direction2));
            return setupUvLock(affineTransformation3);
        }
    }
}
