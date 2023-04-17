package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Minecart;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateMinecart extends Minecart implements ItemTemplate {
    
    public TemplateMinecart(Identifier identifier, int j) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted(), j);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateMinecart(int i, int j) {
        super(i, j);
    }
}
