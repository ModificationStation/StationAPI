package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FireBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFire extends FireBlock implements BlockTemplate {

    public TemplateFire(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFire(int id, int texture) {
        super(id, texture);
    }
}
