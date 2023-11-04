package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.FishingRodItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFishingRodItem extends FishingRodItem implements ItemTemplate {
    public TemplateFishingRodItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateFishingRodItem(int i) {
        super(i);
    }
}
