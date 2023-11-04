package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.NoteBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateNoteblock extends NoteBlock implements BlockTemplate {
    
    public TemplateNoteblock(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateNoteblock(int i) {
        super(i);
    }
}
