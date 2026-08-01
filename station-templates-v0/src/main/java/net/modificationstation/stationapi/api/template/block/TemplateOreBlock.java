package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.OreBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateOreBlock extends OreBlock implements BlockTemplate {
    public TemplateOreBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateOreBlock(int id, int textureId) {
        super(id, textureId);
    }
}
