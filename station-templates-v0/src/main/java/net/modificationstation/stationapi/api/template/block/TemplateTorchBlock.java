package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TorchBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTorchBlock extends TorchBlock implements BlockTemplate {
    public TemplateTorchBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTorchBlock(int i, int j) {
        super(i, j);
    }
}
