package net.modificationstation.stationapi.api.template.item.armour;

import net.minecraft.item.armour.Armour;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateArmour extends Armour implements ItemTemplate {

    public TemplateArmour(Identifier identifier, int j, int k, int slot) {
        this(ItemTemplate.getNextId(), j, k, slot);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateArmour(int id, int j, int k, int slot) {
        super(id, j, k, slot);
    }
}
