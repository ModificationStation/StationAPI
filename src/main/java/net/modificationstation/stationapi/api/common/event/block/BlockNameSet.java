package net.modificationstation.stationapi.api.common.event.block;

import net.minecraft.block.BlockBase;

public class BlockNameSet extends BlockEvent {

    public String newName;

    public BlockNameSet(BlockBase block, String newName) {
        super(block);
        this.newName = newName;
    }
}
