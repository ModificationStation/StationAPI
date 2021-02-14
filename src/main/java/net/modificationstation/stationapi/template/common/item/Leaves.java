package net.modificationstation.stationapi.template.common.item;

public class Leaves extends net.minecraft.item.Leaves {
    
    public Leaves(int i) {
        super(i);
    }

    @Override
    public Leaves setTexturePosition(int texturePosition) {
        return (Leaves) super.setTexturePosition(texturePosition);
    }

    @Override
    public Leaves setMaxStackSize(int newMaxStackSize) {
        return (Leaves) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Leaves setTexturePosition(int x, int y) {
        return (Leaves) super.setTexturePosition(x, y);
    }

    @Override
    public Leaves setHasSubItems(boolean hasSubItems) {
        return (Leaves) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Leaves setDurability(int durability) {
        return (Leaves) super.setDurability(durability);
    }

    @Override
    public Leaves setRendered3d() {
        return (Leaves) super.setRendered3d();
    }

    @Override
    public Leaves setTranslationKey(String newName) {
        return (Leaves) super.setTranslationKey(newName);
    }

    @Override
    public Leaves setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Leaves) super.setContainerItem(itemType);
    }
}
