package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SaplingBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSapling extends SaplingBlock implements BlockTemplate {

    public TemplateSapling(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSapling(int i, int j) {
        super(i, j);
    }
}
