package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Noteblock;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateNoteblock extends Noteblock implements BlockTemplate {
    
    public TemplateNoteblock(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextLegacyId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateNoteblock(int i) {
        super(i);
    }
}
