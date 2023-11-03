package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.CakeBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateCake extends CakeBlock implements BlockTemplate {

    public TemplateCake(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCake(int id, int texture) {
        super(id, texture);
    }
}
