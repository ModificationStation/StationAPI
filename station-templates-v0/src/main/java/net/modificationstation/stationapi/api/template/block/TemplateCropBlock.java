package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.CropBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCropBlock extends CropBlock implements BlockTemplate {
    public TemplateCropBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCropBlock(int id, int textureId) {
        super(id, textureId);
    }
}
