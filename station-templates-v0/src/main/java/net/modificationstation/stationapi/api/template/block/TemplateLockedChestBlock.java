package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LockedChestBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLockedChestBlock extends LockedChestBlock implements BlockTemplate {
    public TemplateLockedChestBlock(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLockedChestBlock(int i) {
        super(i);
    }
}
