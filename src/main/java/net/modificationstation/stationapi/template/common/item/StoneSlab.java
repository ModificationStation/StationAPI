package net.modificationstation.stationapi.template.common.item;

public class StoneSlab extends net.minecraft.item.StoneSlab {
    
    public StoneSlab(int i) {
        super(i);
    }

    @Override
    public StoneSlab setTexturePosition(int texturePosition) {
        return (StoneSlab) super.setTexturePosition(texturePosition);
    }

    @Override
    public StoneSlab setMaxStackSize(int newMaxStackSize) {
        return (StoneSlab) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public StoneSlab setTexturePosition(int x, int y) {
        return (StoneSlab) super.setTexturePosition(x, y);
    }

    @Override
    public StoneSlab setHasSubItems(boolean hasSubItems) {
        return (StoneSlab) super.setHasSubItems(hasSubItems);
    }

    @Override
    public StoneSlab setDurability(int durability) {
        return (StoneSlab) super.setDurability(durability);
    }

    @Override
    public StoneSlab setRendered3d() {
        return (StoneSlab) super.setRendered3d();
    }

    @Override
    public StoneSlab setTranslationKey(String newName) {
        return (StoneSlab) super.setTranslationKey(newName);
    }

    @Override
    public StoneSlab setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (StoneSlab) super.setContainerItem(itemType);
    }
}
