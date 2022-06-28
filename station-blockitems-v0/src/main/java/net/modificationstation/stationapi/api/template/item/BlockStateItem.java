package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.level.HeightLimitView;

public class BlockStateItem extends TemplateItemBase {

    private final BlockState blockState;

    public BlockStateItem(Identifier identifier, BlockState blockState) {
        super(identifier);
        this.blockState = blockState;
    }

    @Override
    public boolean useOnTile(ItemInstance itemInstance, PlayerBase player, Level level, int clickX, int clickY, int clickZ, int side) {
        final Direction direction;
        final int x;
        final int y;
        final int z;
        if (level.getTileId(clickX, clickY, clickZ) == BlockBase.SNOW.id) {
            direction = Direction.DOWN;
            x = clickX;
            y = clickY;
            z = clickZ;
        } else {
            direction = Direction.values()[side];
            x = clickX + direction.vector.x;
            y = clickY + direction.vector.y;
            z = clickZ + direction.vector.z;
        }
        if (itemInstance.count == 0) return false;
        if (y == ((HeightLimitView) level).getTopY() - 1 && blockState.getMaterial().isSolid()) return false;
        BlockBase block = blockState.getBlock();
        if (level.canPlaceTile(block.id, x, y, z, false, direction.getId())) {
            if (StationAPI.EVENT_BUS.post(BlockEvent.BeforePlacedByItem.builder()
                    .level(level)
                    .player(player)
                    .x(x).y(y).z(z)
                    .block(block)
                    .blockItem(itemInstance)
                    .placeFunction(() -> ((BlockStateView) level).setBlockStateWithNotify(x, y, z, blockState) != null)
                    .build()).placeFunction.getAsBoolean()
            ) {
                block.onBlockPlaced(level, x, y, z, direction.getId());
                block.afterPlaced(level, x, y, z, player);
                level.playSound((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f, block.sounds.getWalkSound(), (block.sounds.getVolume() + 1.0f) / 2.0f, block.sounds.getPitch() * 0.8f);
                --itemInstance.count;
            }
            return true;
        }
        return false;
    }
}
