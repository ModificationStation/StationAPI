package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.NoteBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateNoteBlock extends NoteBlock implements BlockTemplate {
    public TemplateNoteBlock(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateNoteBlock(int i) {
        super(i);
    }
}
