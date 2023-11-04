package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PistonBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePistonBlock extends PistonBlock implements BlockTemplate {
    public TemplatePistonBlock(Identifier identifier, int j, boolean flag) {
        this(BlockTemplate.getNextId(), j, flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePistonBlock(int i, int j, boolean flag) {
        super(i, j, flag);
    }
}
