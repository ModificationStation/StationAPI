package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateShovel extends net.minecraft.item.tool.Shovel implements ItemTemplate<TemplateShovel> {
    
    public TemplateShovel(Identifier identifier, ToolMaterial arg) {
        this(ItemRegistry.INSTANCE.getNextSerialIDShifted(), arg);
        ItemRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateShovel(int id, ToolMaterial arg) {
        super(id, arg);
    }

    @Override
    public TemplateShovel setTexturePosition(int texturePosition) {
        return (TemplateShovel) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateShovel setMaxStackSize(int newMaxStackSize) {
        return (TemplateShovel) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateShovel setTexturePosition(int x, int y) {
        return (TemplateShovel) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateShovel setHasSubItems(boolean hasSubItems) {
        return (TemplateShovel) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateShovel setDurability(int durability) {
        return (TemplateShovel) super.setDurability(durability);
    }

    @Override
    public TemplateShovel setRendered3d() {
        return (TemplateShovel) super.setRendered3d();
    }

    @Override
    public TemplateShovel setTranslationKey(String newName) {
        return (TemplateShovel) super.setTranslationKey(newName);
    }

    @Override
    public TemplateShovel setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateShovel) super.setContainerItem(itemType);
    }
}
