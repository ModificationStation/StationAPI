package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RepeaterBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateRepeaterBlock extends RepeaterBlock implements BlockTemplate {
    public TemplateRepeaterBlock(Identifier identifier, boolean lit) {
        this(BlockTemplate.getNextId(), lit);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateRepeaterBlock(int id, boolean lit) {
        super(id, lit);
    }
}
