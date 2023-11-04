package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LadderBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLadderBlock extends LadderBlock implements BlockTemplate {
    public TemplateLadderBlock(Identifier identifier, int texUVStart) {
        this(BlockTemplate.getNextId(), texUVStart);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLadderBlock(int id, int texUVStart) {
        super(id, texUVStart);
    }
}
