package net.modificationstation.stationapi.template.common.item;

public class Sapling extends net.minecraft.item.Sapling implements IItemTemplate<Sapling> {
    
    public Sapling(int i) {
        super(i);
    }

    @Override
    public Sapling setTexturePosition(int texturePosition) {
        return (Sapling) super.setTexturePosition(texturePosition);
    }

    @Override
    public Sapling setMaxStackSize(int newMaxStackSize) {
        return (Sapling) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Sapling setTexturePosition(int x, int y) {
        return (Sapling) super.setTexturePosition(x, y);
    }

    @Override
    public Sapling setHasSubItems(boolean hasSubItems) {
        return (Sapling) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Sapling setDurability(int durability) {
        return (Sapling) super.setDurability(durability);
    }

    @Override
    public Sapling setRendered3d() {
        return (Sapling) super.setRendered3d();
    }

    @Override
    public Sapling setTranslationKey(String newName) {
        return (Sapling) super.setTranslationKey(newName);
    }

    @Override
    public Sapling setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Sapling) super.setContainerItem(itemType);
    }
}
