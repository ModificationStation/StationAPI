package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.OreStorageBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateOreStorageBlock extends OreStorageBlock implements BlockTemplate {
    public TemplateOreStorageBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateOreStorageBlock(int id, int textureId) {
        super(id, textureId);
    }
}
