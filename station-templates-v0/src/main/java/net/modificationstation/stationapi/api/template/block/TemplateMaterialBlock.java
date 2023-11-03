package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.OreStorageBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMaterialBlock extends OreStorageBlock implements BlockTemplate {
    
    public TemplateMaterialBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateMaterialBlock(int i, int j) {
        super(i, j);
    }
}
