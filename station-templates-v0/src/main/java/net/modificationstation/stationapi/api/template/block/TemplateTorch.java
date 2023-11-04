package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TorchBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTorch extends TorchBlock implements BlockTemplate {
    
    public TemplateTorch(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTorch(int i, int j) {
        super(i, j);
    }
}
