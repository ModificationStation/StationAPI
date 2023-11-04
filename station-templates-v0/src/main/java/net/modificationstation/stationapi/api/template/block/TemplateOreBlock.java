package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.OreBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateOreBlock extends OreBlock implements BlockTemplate {
    public TemplateOreBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateOreBlock(int i, int j) {
        super(i, j);
    }
}
