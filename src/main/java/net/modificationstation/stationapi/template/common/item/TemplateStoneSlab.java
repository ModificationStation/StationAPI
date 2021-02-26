package net.modificationstation.stationapi.template.common.item;

public class TemplateStoneSlab extends net.minecraft.item.StoneSlab implements IItemTemplate<TemplateStoneSlab> {
    
    public TemplateStoneSlab(int i) {
        super(i);
    }

    @Override
    public TemplateStoneSlab setTexturePosition(int texturePosition) {
        return (TemplateStoneSlab) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateStoneSlab setMaxStackSize(int newMaxStackSize) {
        return (TemplateStoneSlab) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateStoneSlab setTexturePosition(int x, int y) {
        return (TemplateStoneSlab) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateStoneSlab setHasSubItems(boolean hasSubItems) {
        return (TemplateStoneSlab) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateStoneSlab setDurability(int durability) {
        return (TemplateStoneSlab) super.setDurability(durability);
    }

    @Override
    public TemplateStoneSlab setRendered3d() {
        return (TemplateStoneSlab) super.setRendered3d();
    }

    @Override
    public TemplateStoneSlab setTranslationKey(String newName) {
        return (TemplateStoneSlab) super.setTranslationKey(newName);
    }

    @Override
    public TemplateStoneSlab setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateStoneSlab) super.setContainerItem(itemType);
    }
}
