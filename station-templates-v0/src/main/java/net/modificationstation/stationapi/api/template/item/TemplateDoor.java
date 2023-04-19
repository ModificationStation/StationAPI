package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.material.Material;
import net.minecraft.item.Door;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDoor extends Door implements ItemTemplate {
    
    public TemplateDoor(Identifier identifier, Material arg) {
        this(ItemTemplate.getNextId(), arg);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDoor(int id, Material arg) {
        super(id, arg);
    }
}
