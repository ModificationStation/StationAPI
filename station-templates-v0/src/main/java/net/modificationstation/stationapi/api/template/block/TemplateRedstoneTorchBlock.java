package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneTorchBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateRedstoneTorchBlock extends RedstoneTorchBlock implements BlockTemplate {
    public TemplateRedstoneTorchBlock(Identifier identifier, int j, boolean flag) {
        this(BlockTemplate.getNextId(), j, flag);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateRedstoneTorchBlock(int i, int j, boolean flag) {
        super(i, j, flag);
    }
}
