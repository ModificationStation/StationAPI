package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.Bucket;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateBucket extends Bucket implements ItemTemplate {
    
    public TemplateBucket(Identifier identifier, int j) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted(), j);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBucket(int i, int j) {
        super(i, j);
    }
}
