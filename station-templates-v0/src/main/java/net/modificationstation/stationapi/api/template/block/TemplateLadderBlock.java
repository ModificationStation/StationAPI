package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LadderBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLadderBlock extends LadderBlock implements BlockTemplate {
    public TemplateLadderBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLadderBlock(int id, int textureId) {
        super(id, textureId);
    }
}
