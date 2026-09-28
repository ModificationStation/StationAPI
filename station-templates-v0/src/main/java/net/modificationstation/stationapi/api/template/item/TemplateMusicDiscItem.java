package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.MusicDiscItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateMusicDiscItem extends MusicDiscItem implements ItemTemplate {
    public TemplateMusicDiscItem(Identifier identifier, String sound) {
        this(ItemTemplate.getNextId(), sound);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateMusicDiscItem(int id, String sound) {
        super(id, sound);
    }
}
