package net.modificationstation.stationapi.mixin.block.server;

import net.minecraft.block.Block;
import net.minecraft.class_73;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.math.Box;
import net.modificationstation.stationapi.api.block.HasCollisionVoxelShape;
import net.modificationstation.stationapi.api.block.HasVoxelShape;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow private ServerPlayerEntity field_920;
    @Shadow private MinecraftServer field_919;

    @Redirect(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;method_832(DDDFF)V", ordinal = 0))
    private void stationapi_verifyBlockIntersectionForBlockVoxelShapes(ServerPlayNetworkHandler instance, double e, double f, double g, float h, float v) {
        class_73 world = this.field_919.method_2157(this.field_920.dimensionId);
        Box originalPlayerBox = field_920.boundingBox;
        Box playerBox = Box.createCached(originalPlayerBox.minX, originalPlayerBox.minY, originalPlayerBox.minZ,
                originalPlayerBox.maxX, originalPlayerBox.maxY, originalPlayerBox.maxZ);

        Vec3i min = new Vec3i(Math.floor(playerBox.minX), Math.floor(playerBox.minY), Math.floor(playerBox.minZ));
        Vec3i max = new Vec3i(Math.ceil(playerBox.maxX), Math.ceil(playerBox.maxY), Math.ceil(playerBox.maxZ));

        boolean collisionVerified = false;

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    Block block = world.getBlockState(x, y, z).getBlock();
                    Box[] boxes;

                    if (block instanceof HasVoxelShape hasVoxelShape) {
                        boxes = hasVoxelShape.getVoxelShape(world, x, y, z);
                    } else if (block instanceof HasCollisionVoxelShape hasCollisionVoxelShape) {
                        boxes = hasCollisionVoxelShape.getCollisionVoxelShape(world, x, y, z);
                    } else {
                        boxes = new Box[]{block.getCollisionShape(world, x, y, z)};
                    }

                    for (Box blockBoxPart : boxes) {
                        if (blockBoxPart == null) continue;
                        if (playerBox.intersects(blockBoxPart)) {
                            collisionVerified = true;
                        }
                    }
                }
            }
        }

        if (collisionVerified) {
            instance.method_832(e, f, g, h, v);
        }
    }
}
