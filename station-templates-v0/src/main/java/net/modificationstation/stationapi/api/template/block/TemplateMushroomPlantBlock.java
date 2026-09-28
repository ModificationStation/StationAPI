package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.MushroomPlantBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateMushroomPlantBlock extends MushroomPlantBlock implements BlockTemplate {
    public TemplateMushroomPlantBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateMushroomPlantBlock(int id, int textureId) {
        super(id, textureId);
    }
}
