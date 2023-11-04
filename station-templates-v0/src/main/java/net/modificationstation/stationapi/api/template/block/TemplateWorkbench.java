package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.WorkbenchBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateWorkbench extends WorkbenchBlock implements BlockTemplate {
    
    public TemplateWorkbench(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateWorkbench(int i) {
        super(i);
    }
}
