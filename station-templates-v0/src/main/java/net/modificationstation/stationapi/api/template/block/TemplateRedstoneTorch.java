package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneTorchBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstoneTorch extends RedstoneTorchBlock implements BlockTemplate {
    
    public TemplateRedstoneTorch(Identifier identifier, int j, boolean flag) {
        this(BlockTemplate.getNextId(), j, flag);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateRedstoneTorch(int i, int j, boolean flag) {
        super(i, j, flag);
    }
}
