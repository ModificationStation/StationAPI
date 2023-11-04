package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.OreBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateOre extends OreBlock implements BlockTemplate {

    public TemplateOre(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateOre(int i, int j) {
        super(i, j);
    }
}
