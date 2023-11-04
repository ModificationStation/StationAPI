package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.event.block.IsBlockReplaceableEvent;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class BlockStateItem extends TemplateItem {
    private final BlockState blockState;

    public BlockStateItem(Identifier identifier, BlockState blockState) {
        super(identifier);
        this.blockState = blockState;
    }

    public BlockStateItem(int id, BlockState blockState) {
        super(id);
        this.blockState = blockState;
    }

    @Override
    public boolean useOnBlock(ItemStack itemStack, PlayerEntity player, World world, int clickX, int clickY, int clickZ, int side) {
        final Direction direction;
        final int x;
        final int y;
        final int z;
        if (world.getBlockId(clickX, clickY, clickZ) == Block.SNOW.id) {
            direction = Direction.DOWN;
            side = 0;
            x = clickX;
            y = clickY;
            z = clickZ;
        } else {
            direction = Direction.values()[side];
            x = clickX + direction.getOffsetX();
            y = clickY + direction.getOffsetY();
            z = clickZ + direction.getOffsetZ();
        }
        if (itemStack.count == 0) return false;
        if (y == world.getTopY() - 1 && blockState.getMaterial().method_905()) return false;
        Block block = blockState.getBlock();

        Box box = block.getCollisionShape(world, x, y, z);
        if (
                (box == null || world.canSpawnEntity(box)) && StationAPI.EVENT_BUS.post(
                        IsBlockReplaceableEvent.builder()
                                .context(new ItemPlacementContext(player, itemStack, new HitResult(clickX, clickY, clickZ, side, Vec3d.createCached(x, y, z))))
                                .build()
                ).context.canPlace() && block.canPlaceAt(world, x, y, z, side)
        ) {
            if (StationAPI.EVENT_BUS.post(BlockEvent.BeforePlacedByItem.builder()
                    .world(world)
                    .player(player)
                    .x(x).y(y).z(z)
                    .side(direction)
                    .block(block)
                    .blockItem(itemStack)
                    .placeFunction(() -> world.setBlockStateWithNotify(x, y, z, blockState) != null)
                    .build()).placeFunction.getAsBoolean()
            ) {
                block.onPlaced(world, x, y, z, direction.getId());
                block.onPlaced(world, x, y, z, player);
                world.playSound((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f, block.soundGroup.getSound(), (block.soundGroup.method_1976() + 1.0f) / 2.0f, block.soundGroup.method_1977() * 0.8f);
                --itemStack.count;
            }
            return true;
        }
        return false;
    }
}
