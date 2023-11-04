package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DeadBushBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDeadBush extends DeadBushBlock implements BlockTemplate {

    public TemplateDeadBush(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDeadBush(int i, int j) {
        super(i, j);
    }
}
