package net.modificationstation.stationapi.impl.server.network;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.ChunkMap;
import net.minecraft.world.ServerWorld;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockUpdateS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkSectionDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDeltaUpdateS2CPacket;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import net.modificationstation.stationapi.mixin.flattening.server.ServerPlayerViewAccessor;
import net.modificationstation.stationapi.mixin.flattening.server.TrackedChunkAccessor;

import java.util.List;

/**
 * Collects a chunk's block updates during a tick and sends them to the players watching it, like vanilla's
 * {@link ChunkMap.TrackedChunk}.
 * <p>
 * As in vanilla, it keeps the first 10 distinct positions and a box around all the updates. Then:
 * <ul>
 *     <li>under 10 updates, the positions are sent, one packet per section (see {@link #sendUpdates})</li>
 *     <li>at 10, the box is sent, one {@link FlattenedChunkSectionDataS2CPacket} per section it spans, including
 *     sections in between with no updates (see {@link #sendBox})</li>
 * </ul>
 * The box covers the same blocks as vanilla's chunk data packet. Blocks changed without an update, like the ones tree
 * features place, reach clients only when they happen to be inside such a box.
 * <p>
 * {@code ChunkMapMixin} creates this instead of vanilla's tracked chunk. The chunk position, the update count and
 * the box are vanilla's fields; vanilla's {@code removePlayer} reads the count. The box's {@code x} and {@code z} are
 * local to the chunk, its {@code y} in world coordinates. Vanilla's own position array goes unused: it packs {@code y}
 * into 8 bits, which doesn't fit taller worlds.
 */
public class FlattenedTrackedChunk extends ChunkMap.TrackedChunk {
    private static final int MAX_UPDATES = 10;

    private final ChunkMap chunkMap;

    /** the first 10 distinct positions, packed as {@code y << 8 | x << 4 | z} with {@code y} in world coordinates */
    private final int[] updates = new int[MAX_UPDATES];

    public FlattenedTrackedChunk(ChunkMap chunkMap, int chunkX, int chunkZ) {
        chunkMap.super(chunkX, chunkZ);
        this.chunkMap = chunkMap;
    }

    /**
     * @param x local to the chunk
     * @param y in world coordinates
     * @param z local to the chunk
     */
    @Override
    public void updatePlayerChunks(int x, int y, int z) {
        if (dirtyBlockCount == 0) {
            ((ServerPlayerViewAccessor) chunkMap).getChunksToUpdate().add(this);
            minX = maxX = x;
            minY = maxY = y;
            minZ = maxZ = z;
        }
        if (minX > x) minX = x;
        if (maxX < x) maxX = x;
        if (minY > y) minY = y;
        if (maxY < y) maxY = y;
        if (minZ > z) minZ = z;
        if (maxZ < z) maxZ = z;
        if (dirtyBlockCount < MAX_UPDATES) {
            int pos = y << 8 | x << 4 | z;
            for (int i = 0; i < dirtyBlockCount; i++) if (updates[i] == pos) return;
            updates[dirtyBlockCount++] = pos;
        }
    }

    @Override
    public void updateChunk() {
        if (dirtyBlockCount == 0) return;
        ServerWorld world = chunkMap.getWorld();
        if (dirtyBlockCount == 1) {
            sendBlockChange(world, updates[0]);
            sendBlockEntity(world, updates[0]);
        } else if (dirtyBlockCount == MAX_UPDATES) {
            sendBox(world);
        } else {
            sendUpdates(world);
        }
        dirtyBlockCount = 0;
    }

    /**
     * Sends the updates, one packet per section: a block change for a single update, a multi-block change for more.
     * <p>
     * Then runs vanilla's block entity pass unchanged, bug included: it decodes the update count instead of each
     * position, so it checks the same block once per update, the one at the chunk's corner at a height equal to the
     * count.
     */
    private void sendUpdates(ServerWorld world) {
        int[] updates = this.updates;
        int count = dirtyBlockCount;
        sendGroups(world, updates, count);
        for (int i = 0; i < count; i++) {
            int x = (chunkX << 4) + (count >> 12 & 15);
            int y = count & 0xFF;
            int z = (chunkZ << 4) + (count >> 8 & 15);
            if (!Block.BLOCKS_WITH_ENTITY[world.getBlockId(x, y, z)]) continue;
            System.out.println("Sending!");
            ((TrackedChunkAccessor) this).invokeSendBlockEntityUpdate(world.getBlockEntity(x, y, z));
        }
    }

    private void sendGroups(ServerWorld world, int[] updates, int count) {
        short[] positions = new short[count];
        // updates already sent are set to this, which no position packs to: y would be past any world's height
        int sent = Integer.MIN_VALUE;
        for (int i = 0; i < count; i++) {
            if (updates[i] == sent) continue;
            int sectionY = updates[i] >> 12;
            int n = 0;
            for (int j = i; j < count; j++) {
                int pos = updates[j];
                if (pos == sent || pos >> 12 != sectionY) continue;
                positions[n++] = ChunkSectionPos.packLocal(pos >> 4 & 15, pos >> 8, pos & 15);
                if (j > i) updates[j] = sent;
            }
            if (n == 1) {
                sendBlockChange(world, updates[i]);
            } else {
                sendPacketToPlayers(new FlattenedChunkDeltaUpdateS2CPacket(chunkX, chunkZ, world.getSectionIndex(sectionY << 4), positions, n, world));
            }
        }
    }

    /** @param pos as {@link #updates} holds it */
    private void sendBlockChange(ServerWorld world, int pos) {
        sendPacketToPlayers(new FlattenedBlockUpdateS2CPacket((chunkX << 4) + (pos >> 4 & 15), pos >> 8, (chunkZ << 4) + (pos & 15), world));
    }

    /** @param pos as {@link #updates} holds it */
    private void sendBlockEntity(ServerWorld world, int pos) {
        int x = (chunkX << 4) + (pos >> 4 & 15), y = pos >> 8, z = (chunkZ << 4) + (pos & 15);
        if (Block.BLOCKS_WITH_ENTITY[world.getBlockId(x, y, z)])
            ((TrackedChunkAccessor) this).invokeSendBlockEntityUpdate(world.getBlockEntity(x, y, z));
    }

    /**
     * Sends the box, one packet per section it spans, then the block entities inside it.
     * <p>
     * The box is as tall as vanilla's chunk data packet, which goes past the updates: vanilla rounds the bottom down
     * to an even layer, rounds the top up to an even layer, then adds 2 more. That's 0-1 extra layers below the
     * updates and 2-3 above. Vanilla's block entity query covers the same height.
     */
    private void sendBox(ServerWorld world) {
        // inclusive
        int fromY = Math.max(world.getBottomY(), minY & ~1);
        int toY = Math.min(world.getTopY() - 1, (maxY | 1) + 2);
        int first = world.getSectionIndex(fromY);
        int last = world.getSectionIndex(toY);
        for (int sectionIndex = first; sectionIndex <= last; sectionIndex++) {
            int bottom = world.sectionIndexToCoord(sectionIndex) << 4;
            int sectionFromY = Math.max(fromY, bottom) - bottom;
            int sectionToY = Math.min(toY, bottom + 15) - bottom;
            sendPacketToPlayers(new FlattenedChunkSectionDataS2CPacket(world, chunkX, chunkZ, sectionIndex, minX, sectionFromY, minZ, maxX, sectionToY, maxZ));
        }
        int x = (chunkX << 4) + minX;
        int z = (chunkZ << 4) + minZ;
        //noinspection unchecked
        List<BlockEntity> list = world.getBlockEntities(x, minY & ~1, z, x + maxX - minX + 1, (maxY | 1) + 3, z + maxZ - minZ + 1);
        for (BlockEntity blockEntity : list) ((TrackedChunkAccessor) this).invokeSendBlockEntityUpdate(blockEntity);
    }
}
