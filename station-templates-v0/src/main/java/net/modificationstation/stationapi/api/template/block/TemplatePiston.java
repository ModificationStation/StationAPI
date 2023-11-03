package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PistonBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePiston extends PistonBlock implements BlockTemplate {

    public TemplatePiston(Identifier identifier, int j, boolean flag) {
        this(BlockTemplate.getNextId(), j, flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePiston(int i, int j, boolean flag) {
        super(i, j, flag);
    }
}
