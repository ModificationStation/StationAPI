package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Noteblock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateNoteblock extends Noteblock implements BlockTemplate {
    
    public TemplateNoteblock(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateNoteblock(int i) {
        super(i);
    }
}
