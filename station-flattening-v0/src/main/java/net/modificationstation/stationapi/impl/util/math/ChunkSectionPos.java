package net.modificationstation.stationapi.impl.util.math;

public class ChunkSectionPos {

    public static int getSectionCoord(int coord) {
        return coord >> 4;
    }
}
