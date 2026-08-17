package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PistonHeadBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePistonHeadBlock extends PistonHeadBlock implements BlockTemplate {
    public TemplatePistonHeadBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePistonHeadBlock(int id, int textureId) {
        super(id, textureId);
    }
}
