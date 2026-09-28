package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DeadBushBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDeadBushBlock extends DeadBushBlock implements BlockTemplate {
    public TemplateDeadBushBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDeadBushBlock(int id, int textureId) {
        super(id, textureId);
    }
}
