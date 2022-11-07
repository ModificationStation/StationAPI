package net.modificationstation.stationapi.impl.util;

public class CuboidBlockIterator {
    public static final int field_33084 = 0;
    public static final int field_33085 = 1;
    public static final int field_33086 = 2;
    public static final int field_33087 = 3;
    private final int startX;
    private final int startY;
    private final int startZ;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final int totalSize;
    private int blocksIterated;
    private int x;
    private int y;
    private int z;

    public CuboidBlockIterator(int startX, int startY, int startZ, int endX, int endY, int endZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.sizeX = endX - startX + 1;
        this.sizeY = endY - startY + 1;
        this.sizeZ = endZ - startZ + 1;
        this.totalSize = this.sizeX * this.sizeY * this.sizeZ;
    }

    public boolean step() {
        if (this.blocksIterated == this.totalSize) {
            return false;
        }
        this.x = this.blocksIterated % this.sizeX;
        int i = this.blocksIterated / this.sizeX;
        this.y = i % this.sizeY;
        this.z = i / this.sizeY;
        ++this.blocksIterated;
        return true;
    }

    public int getX() {
        return this.startX + this.x;
    }

    public int getY() {
        return this.startY + this.y;
    }

    public int getZ() {
        return this.startZ + this.z;
    }

    public int getEdgeCoordinatesCount() {
        int i = 0;
        if (this.x == 0 || this.x == this.sizeX - 1) {
            ++i;
        }
        if (this.y == 0 || this.y == this.sizeY - 1) {
            ++i;
        }
        if (this.z == 0 || this.z == this.sizeZ - 1) {
            ++i;
        }
        return i;
    }
}

