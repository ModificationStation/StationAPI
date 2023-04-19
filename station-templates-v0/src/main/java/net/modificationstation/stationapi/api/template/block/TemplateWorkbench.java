package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Workbench;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateWorkbench extends Workbench implements BlockTemplate {
    
    public TemplateWorkbench(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateWorkbench(int i) {
        super(i);
    }
}
