package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PistonHeadBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePistonHead extends PistonHeadBlock implements BlockTemplate {

    public TemplatePistonHead(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePistonHead(int i, int j) {
        super(i, j);
    }
}
