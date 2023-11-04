package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FarmlandBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFarmlandBlock extends FarmlandBlock implements BlockTemplate {
    public TemplateFarmlandBlock(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFarmlandBlock(int id) {
        super(id);
    }
}
