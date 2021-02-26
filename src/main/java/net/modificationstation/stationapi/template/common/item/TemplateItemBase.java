package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TemplateItemBase extends net.minecraft.item.ItemBase implements IItemTemplate<TemplateItemBase> {

    public TemplateItemBase(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateItemBase(int id) {
        super(id);
    }

    @Override
    public TemplateItemBase setTexturePosition(int texturePosition) {
        return (TemplateItemBase) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateItemBase setMaxStackSize(int newMaxStackSize) {
        return (TemplateItemBase) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateItemBase setTexturePosition(int x, int y) {
        return (TemplateItemBase) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateItemBase setHasSubItems(boolean hasSubItems) {
        return (TemplateItemBase) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateItemBase setDurability(int durability) {
        return (TemplateItemBase) super.setDurability(durability);
    }

    @Override
    public TemplateItemBase setRendered3d() {
        return (TemplateItemBase) super.setRendered3d();
    }

    @Override
    public TemplateItemBase setTranslationKey(String newName) {
        return (TemplateItemBase) super.setTranslationKey(newName);
    }

    @Override
    public TemplateItemBase setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateItemBase) super.setContainerItem(itemType);
    }
}
