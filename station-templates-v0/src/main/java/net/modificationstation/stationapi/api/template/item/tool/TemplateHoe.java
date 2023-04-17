package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.Hoe;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateHoe extends Hoe implements ItemTemplate {

    public TemplateHoe(Identifier identifier, ToolMaterial arg) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted(), arg);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateHoe(int i, ToolMaterial arg) {
        super(i, arg);
    }
}
