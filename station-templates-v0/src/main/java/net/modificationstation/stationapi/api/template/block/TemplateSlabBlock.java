package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SlabBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSlabBlock extends SlabBlock implements BlockTemplate {
    public TemplateSlabBlock(Identifier identifier, boolean doubleSlab) {
        this(BlockTemplate.getNextId(), doubleSlab);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSlabBlock(int id, boolean doubleSlab) {
        super(id, doubleSlab);
    }
}
