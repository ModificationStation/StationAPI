package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.AbstractToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.AbstractToolMaterialImpl;

public class TemplateShovelItem extends ShovelItem implements ItemTemplate {
    public TemplateShovelItem(Identifier identifier, AbstractToolMaterial material) {
        this(identifier, AbstractToolMaterialImpl.pushAbstractToolMaterial(material));
    }

    public TemplateShovelItem(int id, AbstractToolMaterial material) {
        this(id, AbstractToolMaterialImpl.pushAbstractToolMaterial(material));
    }

    public TemplateShovelItem(Identifier identifier, ToolMaterial arg) {
        this(ItemTemplate.getNextId(), arg);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateShovelItem(int id, ToolMaterial arg) {
        super(id, arg);
    }
}
