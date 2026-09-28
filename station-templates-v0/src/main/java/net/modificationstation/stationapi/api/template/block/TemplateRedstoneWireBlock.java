package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneWireBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateRedstoneWireBlock extends RedstoneWireBlock implements BlockTemplate {
    public TemplateRedstoneWireBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateRedstoneWireBlock(int id, int textureId) {
        super(id, textureId);
    }
}
