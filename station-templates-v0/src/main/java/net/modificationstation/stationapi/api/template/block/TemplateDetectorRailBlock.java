package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DetectorRailBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDetectorRailBlock extends DetectorRailBlock implements BlockTemplate {
    public TemplateDetectorRailBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDetectorRailBlock(int id, int textureId) {
        super(id, textureId);
    }
}
