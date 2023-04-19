package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Farmland;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFarmland extends Farmland implements BlockTemplate {

    public TemplateFarmland(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFarmland(int id) {
        super(id);
    }
}
