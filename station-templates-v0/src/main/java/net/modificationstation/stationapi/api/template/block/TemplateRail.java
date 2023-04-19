package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Rail;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRail extends Rail implements BlockTemplate {

    public TemplateRail(Identifier identifier, int j, boolean flag) {
        this(BlockTemplate.getNextId(), j, flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateRail(int i, int j, boolean flag) {
        super(i, j, flag);
    }
}
