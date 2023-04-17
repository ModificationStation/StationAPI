package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Painting;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplatePainting extends Painting implements ItemTemplate {
    
    public TemplatePainting(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePainting(int i) {
        super(i);
    }
}
