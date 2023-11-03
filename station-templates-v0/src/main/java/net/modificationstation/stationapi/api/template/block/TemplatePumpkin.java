package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PumpkinBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePumpkin extends PumpkinBlock implements BlockTemplate {

    public TemplatePumpkin(Identifier identifier, int j, boolean flag) {
        this(BlockTemplate.getNextId(), j, flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePumpkin(int i, int j, boolean flag) {
        super(i, j, flag);
    }
}
