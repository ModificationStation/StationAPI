package net.modificationstation.stationapi.api.template.item.armour;

import net.minecraft.item.ArmorItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateArmour extends ArmorItem implements ItemTemplate {

    public TemplateArmour(Identifier identifier, int j, int k, int slot) {
        this(ItemTemplate.getNextId(), j, k, slot);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateArmour(int id, int j, int k, int slot) {
        super(id, j, k, slot);
    }
}
