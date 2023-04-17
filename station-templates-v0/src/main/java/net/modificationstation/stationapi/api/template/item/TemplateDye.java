package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Dye;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateDye extends Dye implements ItemTemplate {
    
    public TemplateDye(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDye(int i) {
        super(i);
    }
}
