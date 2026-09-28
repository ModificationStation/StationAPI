package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.Block;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateToolItem extends ToolItem implements ItemTemplate {
    public TemplateToolItem(Identifier identifier, int damageBoost, ToolMaterial toolMaterial, Block[] effectiveOn) {
        this(ItemTemplate.getNextId(), damageBoost, toolMaterial, effectiveOn);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateToolItem(int id, int damageBoost, ToolMaterial toolMaterial, Block[] effectiveOn) {
        super(id, damageBoost, toolMaterial, effectiveOn);
    }
}
