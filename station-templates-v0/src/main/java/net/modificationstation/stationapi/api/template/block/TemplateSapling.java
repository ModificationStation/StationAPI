package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Sapling;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSapling extends Sapling implements BlockTemplate {

    public TemplateSapling(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSapling(int i, int j) {
        super(i, j);
    }
}
