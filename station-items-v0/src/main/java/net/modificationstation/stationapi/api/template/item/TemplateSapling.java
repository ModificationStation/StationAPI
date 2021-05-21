package net.modificationstation.stationapi.api.template.item;

public class TemplateSapling extends net.minecraft.item.Sapling implements ItemTemplate<TemplateSapling> {
    
    public TemplateSapling(int i) {
        super(i);
    }

    @Override
    public TemplateSapling setTexturePosition(int texturePosition) {
        return (TemplateSapling) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateSapling setMaxStackSize(int newMaxStackSize) {
        return (TemplateSapling) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateSapling setTexturePosition(int x, int y) {
        return (TemplateSapling) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateSapling setHasSubItems(boolean hasSubItems) {
        return (TemplateSapling) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateSapling setDurability(int durability) {
        return (TemplateSapling) super.setDurability(durability);
    }

    @Override
    public TemplateSapling setRendered3d() {
        return (TemplateSapling) super.setRendered3d();
    }

    @Override
    public TemplateSapling setTranslationKey(String newName) {
        return (TemplateSapling) super.setTranslationKey(newName);
    }

    @Override
    public TemplateSapling setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateSapling) super.setContainerItem(itemType);
    }
}
