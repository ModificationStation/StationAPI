package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PumpkinBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePumpkinBlock extends PumpkinBlock implements BlockTemplate {
    public TemplatePumpkinBlock(Identifier identifier, int textureId, boolean lit) {
        this(BlockTemplate.getNextId(), textureId, lit);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePumpkinBlock(int id, int textureId, boolean lit) {
        super(id, textureId, lit);
    }
}
