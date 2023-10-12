package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ItemUsageContext {
    @Nullable
    private final PlayerBase player;
    private final HitResult hit;
    private final Level world;
    private final ItemInstance stack;

    public ItemUsageContext(PlayerBase player, HitResult hit) {
        this(player.level, player, player.getHeldItem(), hit);
    }

    protected ItemUsageContext(Level world, @Nullable PlayerBase player, ItemInstance stack, HitResult hit) {
        this.player = player;
        this.hit = hit;
        this.stack = stack;
        this.world = world;
    }

    protected final HitResult getHitResult() {
        return this.hit;
    }

    public TilePos getBlockPos() {
        return new TilePos(hit.x, hit.y, hit.z);
    }

    public Direction getSide() {
        return Direction.byId(this.hit.field_1987);
    }

    public Vec3d getHitPos() {
        return new Vec3d(hit.field_1988.x, hit.field_1988.y, hit.field_1988.z);
    }

    public boolean hitsInsideBlock() {
        return world.getBlockState(MathHelper.floor(hit.field_1988.x), MathHelper.floor(hit.field_1988.y), MathHelper.floor(hit.field_1988.z)) == States.AIR.get();
    }

    public ItemInstance getStack() {
        return this.stack;
    }

    @Nullable
    public PlayerBase getPlayer() {
        return this.player;
    }

    public Level getWorld() {
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

