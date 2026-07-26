package net.modificationstation.stationapi.impl.world.chunk;

import net.minecraft.world.World;

import java.util.Arrays;

public class CachedFlattenedChunk extends FlattenedChunk {
    public final short[] blockIdCache;
    public final int bottomY;
    // This can be used to figure out if we need an short or int cache
    //private static final int blockRegistrySize = BlockRegistry.INSTANCE.getNextId();

    public CachedFlattenedChunk(World world, int xPos, int zPos) {
        super(world, xPos, zPos);
        blockIdCache = new short[16 * 16 * world.getHeight()];
        bottomY = world.getBottomY();
        Arrays.fill(blockIdCache, (short) -1);
    }

    @Override
    public ChunkSection getOrCreateSection(int y, boolean fillSkyLight) {
        ChunkSection section = super.getOrCreateSection(y, fillSkyLight);
        section.blockIdCache = blockIdCache;
        section.worldBottomY = bottomY;
        return section;
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        int key = x | (z << 4) | ((y - bottomY) << 8);

        if (key < 0 || key >= blockIdCache.length) {
            System.err.println("x = " + x + ", y = " + y + ", z = " + z);
            return 0;
        }

        if (blockIdCache[key] == -1) {
            blockIdCache[key] = (short) super.getBlockId(x, y, z);
        }

        return blockIdCache[key];
    }
}
