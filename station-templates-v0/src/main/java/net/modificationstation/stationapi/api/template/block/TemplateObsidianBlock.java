package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.ObsidianBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateObsidianBlock extends ObsidianBlock implements BlockTemplate {
    public TemplateObsidianBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateObsidianBlock(int id, int textureId) {
        super(id, textureId);
    }
}
