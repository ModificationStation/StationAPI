package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LadderBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLadder extends LadderBlock implements BlockTemplate {

    public TemplateLadder(Identifier identifier, int texUVStart) {
        this(BlockTemplate.getNextId(), texUVStart);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLadder(int id, int texUVStart) {
        super(id, texUVStart);
    }
}
