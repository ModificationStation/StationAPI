package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.OreStorageBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateOreStorageBlock extends OreStorageBlock implements BlockTemplate {
    public TemplateOreStorageBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateOreStorageBlock(int i, int j) {
        super(i, j);
    }
}
