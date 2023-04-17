package net.modificationstation.stationapi.api.template.item.food;

import net.minecraft.item.food.MushroomStew;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateMushroomStew extends MushroomStew implements ItemTemplate {
    
    public TemplateMushroomStew(Identifier identifier, int healAmount) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted(), healAmount);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateMushroomStew(int id, int healAmount) {
        super(id, healAmount);
    }
}
