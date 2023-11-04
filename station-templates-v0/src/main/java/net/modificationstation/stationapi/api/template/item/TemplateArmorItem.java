package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ArmorItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateArmorItem extends ArmorItem implements ItemTemplate {
    public TemplateArmorItem(Identifier identifier, int j, int k, int slot) {
        this(ItemTemplate.getNextId(), j, k, slot);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateArmorItem(int id, int j, int k, int slot) {
        super(id, j, k, slot);
    }
}
