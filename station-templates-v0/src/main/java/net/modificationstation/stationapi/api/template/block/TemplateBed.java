package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BedBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateBed extends BedBlock implements BlockTemplate {

    public TemplateBed(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBed(int id) {
        super(id);
    }
}
