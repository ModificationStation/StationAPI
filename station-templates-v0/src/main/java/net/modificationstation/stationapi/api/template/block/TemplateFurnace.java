package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FurnaceBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFurnace extends FurnaceBlock implements BlockTemplate {

    public TemplateFurnace(Identifier identifier, boolean flag) {
        this(BlockTemplate.getNextId(), flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFurnace(int i, boolean flag) {
        super(i, flag);
    }
}
