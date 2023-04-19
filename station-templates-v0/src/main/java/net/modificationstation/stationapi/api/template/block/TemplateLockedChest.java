package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LockedChest;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateLockedChest extends LockedChest implements BlockTemplate {

    public TemplateLockedChest(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLockedChest(int i) {
        super(i);
    }
}
