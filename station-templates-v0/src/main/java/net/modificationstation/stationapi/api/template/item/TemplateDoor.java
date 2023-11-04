package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.Material;
import net.minecraft.item.DoorItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDoor extends DoorItem implements ItemTemplate {
    
    public TemplateDoor(Identifier identifier, Material arg) {
        this(ItemTemplate.getNextId(), arg);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDoor(int id, Material arg) {
        super(id, arg);
    }
}
