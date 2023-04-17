package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Saddle;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateSaddle extends Saddle implements ItemTemplate {
    
    public TemplateSaddle(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSaddle(int i) {
        super(i);
    }
}
