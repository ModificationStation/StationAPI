package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FurnaceBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFurnaceBlock extends FurnaceBlock implements BlockTemplate {
    public TemplateFurnaceBlock(Identifier identifier, boolean lit) {
        this(BlockTemplate.getNextId(), lit);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFurnaceBlock(int id, boolean lit) {
        super(id, lit);
    }
}
