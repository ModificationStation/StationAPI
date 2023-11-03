package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FarmlandBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFarmland extends FarmlandBlock implements BlockTemplate {

    public TemplateFarmland(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFarmland(int id) {
        super(id);
    }
}
