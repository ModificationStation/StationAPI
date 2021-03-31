package net.modificationstation.stationapi.template.common.item.tool;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.template.common.item.IItemTemplate;

public class TemplateShears extends net.minecraft.item.tool.Shears implements IItemTemplate<TemplateShears> {
    
    public TemplateShears(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateShears(int i) {
        super(i);
    }

    @Override
    public TemplateShears setTexturePosition(int texturePosition) {
        return (TemplateShears) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateShears setMaxStackSize(int newMaxStackSize) {
        return (TemplateShears) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateShears setTexturePosition(int x, int y) {
        return (TemplateShears) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateShears setHasSubItems(boolean hasSubItems) {
        return (TemplateShears) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateShears setDurability(int durability) {
        return (TemplateShears) super.setDurability(durability);
    }

    @Override
    public TemplateShears setRendered3d() {
        return (TemplateShears) super.setRendered3d();
    }

    @Override
    public TemplateShears setTranslationKey(String newName) {
        return (TemplateShears) super.setTranslationKey(newName);
    }

    @Override
    public TemplateShears setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateShears) super.setContainerItem(itemType);
    }
}
