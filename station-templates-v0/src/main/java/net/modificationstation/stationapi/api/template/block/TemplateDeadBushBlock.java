package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DeadBushBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDeadBushBlock extends DeadBushBlock implements BlockTemplate {
    public TemplateDeadBushBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDeadBushBlock(int i, int j) {
        super(i, j);
    }
}
