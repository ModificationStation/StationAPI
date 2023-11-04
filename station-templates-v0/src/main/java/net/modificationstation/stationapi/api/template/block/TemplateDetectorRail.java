package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DetectorRailBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDetectorRail extends DetectorRailBlock implements BlockTemplate {
    
    public TemplateDetectorRail(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDetectorRail(int id, int texture) {
        super(id, texture);
    }
}
