package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ArmorItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateArmorItem extends ArmorItem implements ItemTemplate {
    public TemplateArmorItem(Identifier identifier, int type, int textureIndex, int slot) {
        this(ItemTemplate.getNextId(), type, textureIndex, slot);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateArmorItem(int id, int type, int textureIndex, int slot) {
        super(id, type, textureIndex, slot);
    }
}
