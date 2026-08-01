package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TallPlantBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTallPlantBlock extends TallPlantBlock implements BlockTemplate {
    public TemplateTallPlantBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateTallPlantBlock(int id, int textureId) {
        super(id, textureId);
    }
}
