package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.TilePos;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class ItemPlacementContext
extends ItemUsageContext {
    private final TilePos placementPos;
    protected boolean canReplaceExisting;

    public ItemPlacementContext(PlayerBase player, ItemInstance stack, HitResult hitResult) {
        this(player.level, player, stack, hitResult);
    }

    public ItemPlacementContext(ItemUsageContext context) {
        this(context.getWorld(), context.getPlayer(), context.getStack(), context.getHitResult());
    }

    protected ItemPlacementContext(Level world, @Nullable PlayerBase playerEntity, ItemInstance itemStack, HitResult blockHitResult) {
        super(world, playerEntity, itemStack, blockHitResult);
        this.placementPos = new TilePos(blockHitResult.x, blockHitResult.y, blockHitResult.z).offset(Direction.byId(blockHitResult.field_1987));
        this.canReplaceExisting = world.getBlockState(blockHitResult.x, blockHitResult.y, blockHitResult.z).canReplace(this);
    }

    public static ItemPlacementContext offset(ItemPlacementContext context, TilePos pos, Direction side) {
        return new ItemPlacementContext(context.getWorld(), context.getPlayer(), context.getStack(), new HitResult(pos.getX(), pos.getY(), pos.getZ(), side.getId(), Vec3f.from(pos.getX() + 0.5 + (double)side.getOffsetX() * 0.5, (double)pos.getY() + 0.5 + (double)side.getOffsetY() * 0.5, (double)pos.getZ() + 0.5 + (double)side.getOffsetZ() * 0.5)));
    }

    @Override
    public TilePos getBlockPos() {
        return this.canReplaceExisting ? super.getBlockPos() : this.placementPos;
    }

    public boolean canPlace() {
        return this.canReplaceExisting || this.getWorld().getBlockState(this.getBlockPos()).canReplace(this);
    }

    public boolean canReplaceExisting() {
        return this.canReplaceExisting;
    }

    public Direction getPlayerLookDirection() {
        return Direction.getEntityFacingOrder(this.getPlayer())[0];
    }

    public Direction getVerticalPlayerLookDirection() {
        return Direction.getLookDirectionForAxis(this.getPlayer(), Direction.Axis.Y);
    }

    public Direction[] getPlacementDirections() {
        int i;
        Direction[] directions = Direction.getEntityFacingOrder(this.getPlayer());
        if (this.canReplaceExisting) {
            return directions;
        }
        Direction direction = this.getSide();
        i = 0;
        while (i < directions.length && directions[i] != direction.getOpposite()) {
            ++i;
        }
        if (i > 0) {
            System.arraycopy(directions, 0, directions, 1, i);
            directions[0] = direction.getOpposite();
        }
        return directions;
    }
}

