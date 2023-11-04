package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.MushroomPlantBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateMushroom extends MushroomPlantBlock implements BlockTemplate {

    public TemplateMushroom(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateMushroom(int i, int j) {
        super(i, j);
    }
}
