package net.modificationstation.sltest.block;

import net.minecraft.block.Material;
import net.modificationstation.sltest.item.IndispensableBlockItem;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

@HasCustomBlockItemFactory(IndispensableBlockItem.class)
public class IndispensableBlock extends TemplateBlock {
    public IndispensableBlock(Identifier identifier) {
        super(identifier, Material.WOOD);
    }
}
