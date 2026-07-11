package net.modificationstation.stationapi.impl.world.chunk;

import net.minecraft.world.World;

import java.util.Arrays;

public class CachedFlattenedChunk extends FlattenedChunk {
    public final int[] blockIdCache;
    
    public CachedFlattenedChunk(World world, int xPos, int zPos) {
        super(world, xPos, zPos);
        blockIdCache = new int[16 * 16 * this.world.getHeight()];
        Arrays.fill(blockIdCache, -1);
    }

    @Override
    public ChunkSection getOrCreateSection(int y, boolean fillSkyLight) {
        ChunkSection section =  super.getOrCreateSection(y, fillSkyLight);
        section.blockIdCache = blockIdCache;
        return section;
    }

    @Override
    protected ChunkSection getSection(int y) {
        ChunkSection section = super.getSection(y);
        
        // This never happens
//        if (section != null && section.blockIdCache == null) {
//            section.blockIdCache = blockIdCache;
//            System.err.println("BLOCKIDCACHE NULL");
//        }
        
        return section;
    }

    @Override
    public int getBlockId(int x, int y, int z) {
        int key = x | (z << 4) | (y << 8);

        if (blockIdCache[key] == -1) {
            blockIdCache[key] = super.getBlockId(x, y, z);
        }

        return blockIdCache[key];
    }
}
