package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.Bow;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateBow extends Bow implements ItemTemplate {
    
    public TemplateBow(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBow(int i) {
        super(i);
    }
}
