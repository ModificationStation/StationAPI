package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.AbstractToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.AbstractToolMaterialImpl;

public class TemplateAxeItem extends AxeItem implements ItemTemplate {
    public TemplateAxeItem(Identifier identifier, AbstractToolMaterial material) {
        this(identifier, AbstractToolMaterialImpl.pushAbstractToolMaterial(material));
    }

    public TemplateAxeItem(int id, AbstractToolMaterial material) {
        this(id, AbstractToolMaterialImpl.pushAbstractToolMaterial(material));
    }

    public TemplateAxeItem(Identifier identifier, ToolMaterial material) {
        this(ItemTemplate.getNextId(), material);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateAxeItem(int id, ToolMaterial material) {
        super(id, material);
    }
}
