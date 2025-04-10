package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ItemUsageContext {
    @Nullable
    private final PlayerEntity player;
    private final HitResult hit;
    private final World world;
    private final ItemStack stack;

    public ItemUsageContext(PlayerEntity player, HitResult hit) {
        this(player.world, player, player.getHand(), hit);
    }

    protected ItemUsageContext(World world, @Nullable PlayerEntity player, ItemStack stack, HitResult hit) {
        this.player = player;
        this.hit = hit;
        this.stack = stack;
        this.world = world;
    }

    protected final HitResult getHitResult() {
        return this.hit;
    }

    public BlockPos getBlockPos() {
        return new BlockPos(hit.blockX, hit.blockY, hit.blockZ);
    }

    public Direction getSide() {
        return Direction.byIndex(this.hit.side);
    }

    public Vec3d getHitPos() {
        return new Vec3d(hit.pos.x, hit.pos.y, hit.pos.z);
    }

    public boolean hitsInsideBlock() {
        return world.getBlockState(MathHelper.floor(hit.pos.x), MathHelper.floor(hit.pos.y), MathHelper.floor(hit.pos.z)) == States.AIR.get();
    }

    public ItemStack getStack() {
        return this.stack;
    }

    @Nullable
    public PlayerEntity getPlayer() {
        return this.player;
    }

    public World getWorld() {
        return this.world;
    }

    public Direction getHorizontalPlayerFacing() {
        return this.player == null ? Direction.NORTH : Direction.fromRotation(player.yaw);
    }

    public boolean shouldCancelInteraction() {
        return this.player != null && this.player.method_1373();
    }

    public float getPlayerYaw() {
        return this.player == null ? 0.0f : this.player.yaw;
    }
}

