package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.AbstractToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.AbstractToolMaterialImpl;

public class TemplatePickaxeItem extends PickaxeItem implements ItemTemplate {
    public TemplatePickaxeItem(Identifier identifier, AbstractToolMaterial material) {
        this(identifier, AbstractToolMaterialImpl.pushAbstractToolMaterial(material));
    }

    public TemplatePickaxeItem(int id, AbstractToolMaterial material) {
        this(id, AbstractToolMaterialImpl.pushAbstractToolMaterial(material));
    }

    public TemplatePickaxeItem(Identifier identifier, ToolMaterial material) {
        this(ItemTemplate.getNextId(), material);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePickaxeItem(int id, ToolMaterial material) {
        super(id, material);
    }
}
