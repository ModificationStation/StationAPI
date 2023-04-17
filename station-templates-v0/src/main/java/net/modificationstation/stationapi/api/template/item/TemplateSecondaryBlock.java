package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.SecondaryBlock;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateSecondaryBlock extends SecondaryBlock implements ItemTemplate {

    public TemplateSecondaryBlock(Identifier identifier, BlockBase tile) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted(), tile);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateSecondaryBlock(int id, BlockBase tile) {
        super(id, tile);
    }
}
