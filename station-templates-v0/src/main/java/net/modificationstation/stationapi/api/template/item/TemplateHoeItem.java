package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.HoeItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.AbstractToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.AbstractToolMaterialImpl;

public class TemplateHoeItem extends HoeItem implements ItemTemplate {
    public TemplateHoeItem(Identifier identifier, AbstractToolMaterial arg) {
        this(identifier, AbstractToolMaterialImpl.pushAbstractToolMaterial(arg));
    }

    public TemplateHoeItem(int i, AbstractToolMaterial arg) {
        this(i, AbstractToolMaterialImpl.pushAbstractToolMaterial(arg));
    }

    public TemplateHoeItem(Identifier identifier, ToolMaterial arg) {
        this(ItemTemplate.getNextId(), arg);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateHoeItem(int i, ToolMaterial arg) {
        super(i, arg);
    }
}
