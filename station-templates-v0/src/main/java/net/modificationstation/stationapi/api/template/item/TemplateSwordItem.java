package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.AbstractToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.AbstractToolMaterialImpl;

public class TemplateSwordItem extends SwordItem implements ItemTemplate {
    public TemplateSwordItem(Identifier identifier, AbstractToolMaterial arg) {
        this(identifier, AbstractToolMaterialImpl.pushAbstractToolMaterial(arg));
    }

    public TemplateSwordItem(int i, AbstractToolMaterial arg) {
        this(i, AbstractToolMaterialImpl.pushAbstractToolMaterial(arg));
    }

    public TemplateSwordItem(Identifier identifier, ToolMaterial arg) {
        this(ItemTemplate.getNextId(), arg);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSwordItem(int i, ToolMaterial arg) {
        super(i, arg);
    }
}
