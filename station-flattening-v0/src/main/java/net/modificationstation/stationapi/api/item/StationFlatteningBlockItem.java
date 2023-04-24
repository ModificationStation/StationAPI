package net.modificationstation.stationapi.api.item;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;

public interface StationFlatteningBlockItem extends BlockItemForm {

    default BlockBase getBlock() {
        return Util.assertImpl();
    }

    @ApiStatus.Internal
    default void setBlock(BlockBase block) {
        Util.assertImpl();
    }
}
