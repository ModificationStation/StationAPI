package net.modificationstation.stationapi.api.worldgen.surface.condition;

public class HeightSurfaceCondition extends PositionSurfaceCondition {
    public HeightSurfaceCondition(int minY, int maxY) {
        super((pos) -> pos.y >= minY && pos.y <= maxY);
    }
}
