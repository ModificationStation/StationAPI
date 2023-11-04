package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FurnaceBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFurnaceBlock extends FurnaceBlock implements BlockTemplate {
    public TemplateFurnaceBlock(Identifier identifier, boolean flag) {
        this(BlockTemplate.getNextId(), flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFurnaceBlock(int i, boolean flag) {
        super(i, flag);
    }
}
