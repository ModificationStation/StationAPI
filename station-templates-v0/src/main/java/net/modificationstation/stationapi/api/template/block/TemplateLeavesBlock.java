package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LeavesBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLeavesBlock extends LeavesBlock implements BlockTemplate {
    public TemplateLeavesBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLeavesBlock(int id, int textureId) {
        super(id, textureId);
    }
}
