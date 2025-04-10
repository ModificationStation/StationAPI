package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import org.joml.Vector3f;

public class SlopeSurfaceCondition implements SurfaceCondition {
    private final Vector3f a = new Vector3f();
    private final Vector3f b = new Vector3f();
    private final boolean greater;
    private final float angle;

    public SlopeSurfaceCondition(float angle, boolean degrees, boolean greater) {
        this.angle = degrees ? (float) Math.toRadians(angle) : angle;
        this.greater = greater;
    }

    @Override
    public boolean canApply(World world, int x, int y, int z, BlockState state) {
        a.set(2, world.getTopY(x + 1, z) - world.getTopY(x - 1, z), 0);
        a.normalize();

        b.set(0, world.getTopY(x, z + 1) - world.getTopY(x, z - 1), 2);
        b.normalize();

        a.cross(b);
        a.normalize();

        b.set(a.x(), 0, a.z());
        b.normalize();

        float dot = a.dot(b);
        return greater ? angle < dot : angle > dot;
    }
}
