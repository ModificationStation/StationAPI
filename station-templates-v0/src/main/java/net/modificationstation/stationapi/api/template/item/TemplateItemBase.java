package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateItemBase extends ItemBase implements ItemTemplate {

    public TemplateItemBase(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateItemBase(int id) {
        super(id);
    }
}
