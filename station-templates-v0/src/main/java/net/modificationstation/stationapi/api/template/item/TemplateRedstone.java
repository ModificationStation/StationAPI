package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Redstone;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateRedstone extends Redstone implements ItemTemplate {
    
    public TemplateRedstone(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateRedstone(int i) {
        super(i);
    }
}
