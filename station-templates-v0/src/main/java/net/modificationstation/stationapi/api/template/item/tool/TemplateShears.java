package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.Shears;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateShears extends Shears implements ItemTemplate {
    
    public TemplateShears(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateShears(int i) {
        super(i);
    }
}
