package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.Block;
import net.minecraft.item.SecondaryBlockItem;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSecondaryBlock extends SecondaryBlockItem implements ItemTemplate {

    public TemplateSecondaryBlock(Identifier identifier, Block tile) {
        this(ItemTemplate.getNextId(), tile);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateSecondaryBlock(int id, Block tile) {
        super(id, tile);
    }
}
