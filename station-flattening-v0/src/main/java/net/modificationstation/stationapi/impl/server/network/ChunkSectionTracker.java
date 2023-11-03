package net.modificationstation.stationapi.impl.server.network;

import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.class_167;
import net.minecraft.class_73;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkSectionDataS2CPacket;
import net.modificationstation.stationapi.impl.packet.FlattenedMultiBlockChangeS2CPacket;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import net.modificationstation.stationapi.mixin.flattening.server.ServerPlayerViewAccessor;
import net.modificationstation.stationapi.mixin.flattening.server.class_514Accessor;

import java.util.List;

@RequiredArgsConstructor
public class ChunkSectionTracker {
    private final class_167 playerView;
    private final class_167.class_514 chunkTracker;
    private final int
            chunkX,
            chunkZ,
            sectionIndex;

    private final short[] updates = new short[10];
    private int updatesCount = 0;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;

    public void queueUpdate(int x, int y, int z) {
        if (this.updatesCount == 0) {
            ((ServerPlayerViewAccessor) playerView).getField_2131().add(chunkTracker);
            this.minX = this.maxX = x;
            this.minY = this.maxY = y;
            this.minZ = this.maxZ = z;
        }
        if (this.minX > x) {
            this.minX = x;
        }
        if (this.maxX < x) {
            this.maxX = x;
        }
        if (this.minY > y) {
            this.minY = y;
        }
        if (this.maxY < y) {
            this.maxY = y;
        }
        if (this.minZ > z) {
            this.minZ = z;
        }
        if (this.maxZ < z) {
            this.maxZ = z;
        }
        if (this.updatesCount < 10) {
            short s = ChunkSectionPos.packLocal(x, y, z);
            for (int i2 = 0; i2 < this.updatesCount; ++i2) {
                if (this.updates[i2] != s) continue;
                return;
            }
            this.updates[this.updatesCount++] = s;
        }
    }

    public void sendQueue() {
        class_73 world = playerView.method_1741();
        int sectionY = world.sectionIndexToCoord(sectionIndex);
        if (this.updatesCount == 0) {
            return;
        }
        if (this.updatesCount == 1) {
            int x = (this.chunkX << 4) + this.minX;
            int y = (sectionY << 4) + this.minY;
            int z = (this.chunkZ << 4) + this.minZ;
            chunkTracker.method_1755(new FlattenedBlockChangeS2CPacket(x, y, z, world));
            if (Block.BLOCKS_WITH_ENTITY[world.getBlockId(x, y, z)]) {
                ((class_514Accessor) chunkTracker).invokeMethod_1756(world.method_1777(x, y, z));
            }
        } else if (this.updatesCount == 10) {
            this.minY = this.minY / 2 * 2;
            this.maxY = (this.maxY / 2 + 1) * 2;
            int x = (this.chunkX << 4) + this.minX;
            int y = (sectionY << 4) + this.minY;
            int z = (this.chunkZ << 4) + this.minZ;
            int sizeX = this.maxX - this.minX + 1;
            int sizeY = this.maxY - this.minY + 2;
            int sizeZ = this.maxZ - this.minZ + 1;
            chunkTracker.method_1755(new FlattenedChunkSectionDataS2CPacket(world, chunkX, chunkZ, sectionIndex));
            //noinspection unchecked
            List<BlockEntity> list = world.method_330(x, y, z, x + sizeX, y + sizeY, z + sizeZ);
            for (int i = 0; i < list.size(); ++i) {
                ((class_514Accessor) chunkTracker).invokeMethod_1756(list.get(i));
            }
        } else {
            chunkTracker.method_1755(new FlattenedMultiBlockChangeS2CPacket(this.chunkX, this.chunkZ, sectionIndex, this.updates, this.updatesCount, world));
            for (int i = 0; i < this.updatesCount; ++i) {
                int n = this.chunkX * 16 + (this.updatesCount >> 12 & 0xF);
                int n9 = this.updatesCount & 0xFF;
                int n10 = this.chunkZ * 16 + (this.updatesCount >> 8 & 0xF);
                if (!Block.BLOCKS_WITH_ENTITY[world.getBlockId(n, n9, n10)]) continue;
                System.out.println("Sending!");
                ((class_514Accessor) chunkTracker).invokeMethod_1756(world.method_1777(n, n9, n10));
            }
        }
        this.updatesCount = 0;
    }
}
