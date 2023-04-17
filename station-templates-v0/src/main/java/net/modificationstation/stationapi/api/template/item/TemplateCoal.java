package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Coal;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateCoal extends Coal implements ItemTemplate {
    
    public TemplateCoal(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateCoal(int i) {
        super(i);
    }
}
