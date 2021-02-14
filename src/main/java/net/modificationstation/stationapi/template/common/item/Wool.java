package net.modificationstation.stationapi.template.common.item;

public class Wool extends net.minecraft.item.Wool {

    public Wool(int i) {
        super(i);
    }

    @Override
    public Wool setTexturePosition(int texturePosition) {
        return (Wool) super.setTexturePosition(texturePosition);
    }

    @Override
    public Wool setMaxStackSize(int newMaxStackSize) {
        return (Wool) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Wool setTexturePosition(int x, int y) {
        return (Wool) super.setTexturePosition(x, y);
    }

    @Override
    public Wool setHasSubItems(boolean hasSubItems) {
        return (Wool) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Wool setDurability(int durability) {
        return (Wool) super.setDurability(durability);
    }

    @Override
    public Wool setRendered3d() {
        return (Wool) super.setRendered3d();
    }

    @Override
    public Wool setTranslationKey(String newName) {
        return (Wool) super.setTranslationKey(newName);
    }

    @Override
    public Wool setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Wool) super.setContainerItem(itemType);
    }
}
