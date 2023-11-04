package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.ChestBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateChest extends ChestBlock implements BlockTemplate {

    public TemplateChest(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateChest(int id) {
        super(id);
    }
}
