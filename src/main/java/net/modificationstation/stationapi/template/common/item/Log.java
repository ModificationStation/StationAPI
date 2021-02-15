package net.modificationstation.stationapi.template.common.item;

public class Log extends net.minecraft.item.Log implements IItemTemplate<Log> {
    
    public Log(int i) {
        super(i);
    }

    @Override
    public Log setTexturePosition(int texturePosition) {
        return (Log) super.setTexturePosition(texturePosition);
    }

    @Override
    public Log setMaxStackSize(int newMaxStackSize) {
        return (Log) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Log setTexturePosition(int x, int y) {
        return (Log) super.setTexturePosition(x, y);
    }

    @Override
    public Log setHasSubItems(boolean hasSubItems) {
        return (Log) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Log setDurability(int durability) {
        return (Log) super.setDurability(durability);
    }

    @Override
    public Log setRendered3d() {
        return (Log) super.setRendered3d();
    }

    @Override
    public Log setTranslationKey(String newName) {
        return (Log) super.setTranslationKey(newName);
    }

    @Override
    public Log setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Log) super.setContainerItem(itemType);
    }
}
