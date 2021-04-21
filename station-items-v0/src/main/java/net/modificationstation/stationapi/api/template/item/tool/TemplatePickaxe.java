package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.IItemTemplate;

public class TemplatePickaxe extends net.minecraft.item.tool.Pickaxe implements IItemTemplate<TemplatePickaxe> {
    
    public TemplatePickaxe(Identifier identifier, ToolMaterial material) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), material);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplatePickaxe(int id, ToolMaterial material) {
        super(id, material);
    }

    @Override
    public TemplatePickaxe setTexturePosition(int texturePosition) {
        return (TemplatePickaxe) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplatePickaxe setMaxStackSize(int newMaxStackSize) {
        return (TemplatePickaxe) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplatePickaxe setTexturePosition(int x, int y) {
        return (TemplatePickaxe) super.setTexturePosition(x, y);
    }

    @Override
    public TemplatePickaxe setHasSubItems(boolean hasSubItems) {
        return (TemplatePickaxe) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplatePickaxe setDurability(int durability) {
        return (TemplatePickaxe) super.setDurability(durability);
    }

    @Override
    public TemplatePickaxe setRendered3d() {
        return (TemplatePickaxe) super.setRendered3d();
    }

    @Override
    public TemplatePickaxe setTranslationKey(String newName) {
        return (TemplatePickaxe) super.setTranslationKey(newName);
    }

    @Override
    public TemplatePickaxe setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplatePickaxe) super.setContainerItem(itemType);
    }
}
