package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LockedChestBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLockedChest extends LockedChestBlock implements BlockTemplate {

    public TemplateLockedChest(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLockedChest(int i) {
        super(i);
    }
}
