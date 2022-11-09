/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.modificationstation.stationapi.impl.util.math;

import it.unimi.dsi.fastutil.longs.LongConsumer;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Position;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import net.modificationstation.stationapi.impl.util.CuboidBlockIterator;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ChunkSectionPos
        extends Vec3i {
    public static final int field_33096 = 4;
    public static final int field_33097 = 16;
    public static final int field_33100 = 15;
    public static final int field_33098 = 8;
    public static final int field_33099 = 15;
    private static final int field_33101 = 22;
    private static final int field_33102 = 20;
    private static final int field_33103 = 22;
    private static final long field_33104 = 0x3FFFFFL;
    private static final long field_33105 = 1048575L;
    private static final long field_33106 = 0x3FFFFFL;
    private static final int field_33107 = 0;
    private static final int field_33108 = 20;
    private static final int field_33109 = 42;
    private static final int field_33110 = 8;
    private static final int field_33111 = 0;
    private static final int field_33112 = 4;

    ChunkSectionPos(int i, int j, int k) {
        super(i, j, k);
    }

    /**
     * Creates a chunk section position from its x-, y- and z-coordinates.
     */
    public static ChunkSectionPos from(int x, int y, int z) {
        return new ChunkSectionPos(x, y, z);
    }

    public static ChunkSectionPos from(BlockPos pos) {
        return new ChunkSectionPos(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getY()), ChunkSectionPos.getSectionCoord(pos.getZ()));
    }

    /**
     * Creates a chunk section position from a chunk position and the y-coordinate of the vertical section.
     */
    public static ChunkSectionPos from(ChunkPos chunkPos, int y) {
        return new ChunkSectionPos(chunkPos.x, y, chunkPos.z);
    }

//    public static ChunkSectionPos from(EntityLike entity) {
//        return ChunkSectionPos.from(entity.getBlockPos());
//    }

    public static ChunkSectionPos from(Position pos) {
        return new ChunkSectionPos(ChunkSectionPos.getSectionCoordFloored(pos.getX()), ChunkSectionPos.getSectionCoordFloored(pos.getY()), ChunkSectionPos.getSectionCoordFloored(pos.getZ()));
    }

    /**
     * Creates a chunk section position from its packed representation.
     * @see #asLong
     */
    public static ChunkSectionPos from(long packed) {
        return new ChunkSectionPos(ChunkSectionPos.unpackX(packed), ChunkSectionPos.unpackY(packed), ChunkSectionPos.unpackZ(packed));
    }

    public static ChunkSectionPos from(Chunk chunk) {
        return ChunkSectionPos.from(new ChunkPos(chunk.x, chunk.z), chunk.level.getBottomSectionCoord());
    }

    /**
     * Offsets a packed chunk section position in the given direction.
     * @see #asLong
     */
    public static long offset(long packed, Direction direction) {
        return ChunkSectionPos.offset(packed, direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
    }

    /**
     * Offsets a packed chunk section position by the given offsets.
     * @see #asLong
     */
    public static long offset(long packed, int x, int y, int z) {
        return ChunkSectionPos.asLong(ChunkSectionPos.unpackX(packed) + x, ChunkSectionPos.unpackY(packed) + y, ChunkSectionPos.unpackZ(packed) + z);
    }

    public static int getSectionCoord(double coord) {
        return ChunkSectionPos.getSectionCoord(MathHelper.floor(coord));
    }

    /**
     * Converts a world coordinate to the corresponding chunk-section coordinate.
     *
     * @implNote This implementation returns {@code coord / 16}.
     */
    public static int getSectionCoord(int coord) {
        return coord >> 4;
    }

    public static int getSectionCoordFloored(double coord) {
        return MathHelper.floor(coord) >> 4;
    }

    /**
     * Converts a world coordinate to the local coordinate system (0-15) of its corresponding chunk section.
     */
    public static int getLocalCoord(int coord) {
        return coord & 0xF;
    }

    /**
     * Returns the local position of the given block position relative to
     * its respective chunk section, packed into a short.
     */
    public static short packLocal(BlockPos pos) {
        int i = ChunkSectionPos.getLocalCoord(pos.getX());
        int j = ChunkSectionPos.getLocalCoord(pos.getY());
        int k = ChunkSectionPos.getLocalCoord(pos.getZ());
        return (short)(i << 8 | k << 4 | j);
    }

    /**
     * Gets the local x-coordinate from the given packed local position.
     * @see #packLocal
     */
    public static int unpackLocalX(short packedLocalPos) {
        return packedLocalPos >>> 8 & 0xF;
    }

    /**
     * Gets the local y-coordinate from the given packed local position.
     * @see #packLocal
     */
    public static int unpackLocalY(short packedLocalPos) {
        return packedLocalPos & 0xF;
    }

    /**
     * Gets the local z-coordinate from the given packed local position.
     * @see #packLocal
     */
    public static int unpackLocalZ(short packedLocalPos) {
        return packedLocalPos >>> 4 & 0xF;
    }

    /**
     * Gets the world x-coordinate of the given local position within this chunk section.
     * @see #packLocal
     */
    public int unpackBlockX(short packedLocalPos) {
        return getMinX() + ChunkSectionPos.unpackLocalX(packedLocalPos);
    }

    /**
     * Gets the world y-coordinate of the given local position within this chunk section.
     * @see #packLocal
     */
    public int unpackBlockY(short packedLocalPos) {
        return getMinY() + ChunkSectionPos.unpackLocalY(packedLocalPos);
    }

    /**
     * Gets the world z-coordinate of the given local position within this chunk section.
     * @see #packLocal
     */
    public int unpackBlockZ(short packedLocalPos) {
        return getMinZ() + ChunkSectionPos.unpackLocalZ(packedLocalPos);
    }

    /**
     * Gets the world position of the given local position within this chunk section.
     * @see #packLocal
     */
    public BlockPos unpackBlockPos(short packedLocalPos) {
        return new BlockPos(unpackBlockX(packedLocalPos), unpackBlockY(packedLocalPos), unpackBlockZ(packedLocalPos));
    }

    /**
     * Converts the given chunk section coordinate to the world coordinate system.
     * The returned coordinate will always be at the origin of the chunk section in world space.
     */
    public static int getBlockCoord(int sectionCoord) {
        return sectionCoord << 4;
    }

    public static int getOffsetPos(int chunkCoord, int offset) {
        return ChunkSectionPos.getBlockCoord(chunkCoord) + offset;
    }

    /**
     * Gets the chunk section x-coordinate from the given packed chunk section coordinate.
     * @see #asLong
     */
    public static int unpackX(long packed) {
        return (int)(packed >> 42);
    }

    /**
     * Gets the chunk section y-coordinate from the given packed chunk section coordinate.
     * @see #asLong
     */
    public static int unpackY(long packed) {
        return (int)(packed << 44 >> 44);
    }

    /**
     * Gets the chunk section z-coordinate from the given packed chunk section coordinate.
     * @see #asLong
     */
    public static int unpackZ(long packed) {
        return (int)(packed << 22 >> 42);
    }

    public int getSectionX() {
        return getX();
    }

    public int getSectionY() {
        return getY();
    }

    public int getSectionZ() {
        return getZ();
    }

    public int getMinX() {
        return ChunkSectionPos.getBlockCoord(getSectionX());
    }

    public int getMinY() {
        return ChunkSectionPos.getBlockCoord(getSectionY());
    }

    public int getMinZ() {
        return ChunkSectionPos.getBlockCoord(getSectionZ());
    }

    public int getMaxX() {
        return ChunkSectionPos.getOffsetPos(getSectionX(), 15);
    }

    public int getMaxY() {
        return ChunkSectionPos.getOffsetPos(getSectionY(), 15);
    }

    public int getMaxZ() {
        return ChunkSectionPos.getOffsetPos(getSectionZ(), 15);
    }

    /**
     * Gets the packed chunk section coordinate for a given packed {@link BlockPos}.
     * @see #asLong
     * @see BlockPos#asLong
     */
    public static long fromBlockPos(long blockPos) {
        return ChunkSectionPos.asLong(ChunkSectionPos.getSectionCoord(BlockPos.unpackLongX(blockPos)), ChunkSectionPos.getSectionCoord(BlockPos.unpackLongY(blockPos)), ChunkSectionPos.getSectionCoord(BlockPos.unpackLongZ(blockPos)));
    }

    /**
     * Gets the packed chunk section coordinate at y=0 for the same chunk as
     * the given packed chunk section coordinate.
     * @see #asLong
     */
    public static long withZeroY(long pos) {
        return pos & 0xFFFFFFFFFFF00000L;
    }

    public BlockPos getMinPos() {
        return new BlockPos(ChunkSectionPos.getBlockCoord(getSectionX()), ChunkSectionPos.getBlockCoord(getSectionY()), ChunkSectionPos.getBlockCoord(getSectionZ()));
    }

    public BlockPos getCenterPos() {
        return getMinPos().add(8, 8, 8);
    }

    public ChunkPos toChunkPos() {
        return new ChunkPos(getSectionX(), getSectionZ());
    }

    public static long toLong(BlockPos pos) {
        return ChunkSectionPos.asLong(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getY()), ChunkSectionPos.getSectionCoord(pos.getZ()));
    }

    public static long asLong(int x, int y, int z) {
        long l = 0L;
        l |= ((long)x & field_33104) << 42;
        l |= ((long) y & field_33105);
        return l |= ((long)z & field_33106) << 20;
    }

    public long asLong() {
        return ChunkSectionPos.asLong(getSectionX(), getSectionY(), getSectionZ());
    }

    @Override
    public ChunkSectionPos add(int i, int j, int k) {
        if (i == 0 && j == 0 && k == 0) {
            return this;
        }
        return new ChunkSectionPos(getSectionX() + i, getSectionY() + j, getSectionZ() + k);
    }

    public Stream<BlockPos> streamBlocks() {
        return BlockPos.stream(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
    }

    public static Stream<ChunkSectionPos> stream(ChunkSectionPos center, int radius) {
        int i = center.getSectionX();
        int j = center.getSectionY();
        int k = center.getSectionZ();
        return ChunkSectionPos.stream(i - radius, j - radius, k - radius, i + radius, j + radius, k + radius);
    }

    public static Stream<ChunkSectionPos> stream(ChunkPos center, int radius, int minY, int maxY) {
        int i = center.x;
        int j = center.z;
        return ChunkSectionPos.stream(i - radius, minY, j - radius, i + radius, maxY - 1, j + radius);
    }

    public static Stream<ChunkSectionPos> stream(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<>((long) (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1), Spliterator.SIZED) {
            final CuboidBlockIterator iterator = new CuboidBlockIterator(minX, minY, minZ, maxX, maxY, maxZ);

            @Override
            public boolean tryAdvance(Consumer<? super ChunkSectionPos> consumer) {
                if (iterator.step()) {
                    consumer.accept(new ChunkSectionPos(iterator.getX(), iterator.getY(), iterator.getZ()));
                    return true;
                }
                return false;
            }
        }, false);
    }

    public static void forEachChunkSectionAround(BlockPos pos, LongConsumer consumer) {
        ChunkSectionPos.forEachChunkSectionAround(pos.getX(), pos.getY(), pos.getZ(), consumer);
    }

    public static void forEachChunkSectionAround(long pos, LongConsumer consumer) {
        ChunkSectionPos.forEachChunkSectionAround(BlockPos.unpackLongX(pos), BlockPos.unpackLongY(pos), BlockPos.unpackLongZ(pos), consumer);
    }

    /**
     * Performs an action for each chunk section enclosing a block position
     * adjacent to {@code (x, y, z)}.
     *
     * @param consumer the consumer that takes the chunk section position as a long
     */
    public static void forEachChunkSectionAround(int x, int y, int z, LongConsumer consumer) {
        int i = ChunkSectionPos.getSectionCoord(x - 1);
        int j = ChunkSectionPos.getSectionCoord(x + 1);
        int k = ChunkSectionPos.getSectionCoord(y - 1);
        int l = ChunkSectionPos.getSectionCoord(y + 1);
        int m = ChunkSectionPos.getSectionCoord(z - 1);
        int n = ChunkSectionPos.getSectionCoord(z + 1);
        if (i == j && k == l && m == n) {
            consumer.accept(ChunkSectionPos.asLong(i, k, m));
        } else {
            for (int o = i; o <= j; ++o) {
                for (int p = k; p <= l; ++p) {
                    for (int q = m; q <= n; ++q) {
                        consumer.accept(ChunkSectionPos.asLong(o, p, q));
                    }
                }
            }
        }
    }
}

