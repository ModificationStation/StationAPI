package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.FishingRodItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateFishingRod extends FishingRodItem implements ItemTemplate {
    
    public TemplateFishingRod(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateFishingRod(int i) {
        super(i);
    }
}
