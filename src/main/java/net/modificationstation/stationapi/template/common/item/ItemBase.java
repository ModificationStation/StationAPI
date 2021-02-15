package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class ItemBase extends net.minecraft.item.ItemBase implements IItemTemplate<ItemBase> {

    public ItemBase(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public ItemBase(int id) {
        super(id);
    }

    @Override
    public ItemBase setTexturePosition(int texturePosition) {
        return (ItemBase) super.setTexturePosition(texturePosition);
    }

    @Override
    public ItemBase setMaxStackSize(int newMaxStackSize) {
        return (ItemBase) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public ItemBase setTexturePosition(int x, int y) {
        return (ItemBase) super.setTexturePosition(x, y);
    }

    @Override
    public ItemBase setHasSubItems(boolean hasSubItems) {
        return (ItemBase) super.setHasSubItems(hasSubItems);
    }

    @Override
    public ItemBase setDurability(int durability) {
        return (ItemBase) super.setDurability(durability);
    }

    @Override
    public ItemBase setRendered3d() {
        return (ItemBase) super.setRendered3d();
    }

    @Override
    public ItemBase setTranslationKey(String newName) {
        return (ItemBase) super.setTranslationKey(newName);
    }

    @Override
    public ItemBase setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (ItemBase) super.setContainerItem(itemType);
    }
}
