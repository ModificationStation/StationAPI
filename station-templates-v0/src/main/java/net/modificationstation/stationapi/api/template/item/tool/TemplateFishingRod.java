package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.FishingRod;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateFishingRod extends FishingRod implements ItemTemplate {
    
    public TemplateFishingRod(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateFishingRod(int i) {
        super(i);
    }
}
