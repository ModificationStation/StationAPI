package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.JukeboxBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateJukebox extends JukeboxBlock implements BlockTemplate {
    
    public TemplateJukebox(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateJukebox(int i, int j) {
        super(i, j);
    }
}
