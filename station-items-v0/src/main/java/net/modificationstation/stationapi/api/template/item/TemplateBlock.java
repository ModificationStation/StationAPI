package net.modificationstation.stationapi.api.template.item;

public class TemplateBlock extends net.minecraft.item.Block implements ItemTemplate<TemplateBlock> {
    
    public TemplateBlock(int i) {
        super(i);
    }

    @Override
    public TemplateBlock setTexturePosition(int texturePosition) {
        return (TemplateBlock) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateBlock setMaxStackSize(int newMaxStackSize) {
        return (TemplateBlock) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateBlock setTexturePosition(int x, int y) {
        return (TemplateBlock) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateBlock setHasSubItems(boolean hasSubItems) {
        return (TemplateBlock) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateBlock setDurability(int durability) {
        return (TemplateBlock) super.setDurability(durability);
    }

    @Override
    public TemplateBlock setRendered3d() {
        return (TemplateBlock) super.setRendered3d();
    }

    @Override
    public TemplateBlock setTranslationKey(String newName) {
        return (TemplateBlock) super.setTranslationKey(newName);
    }

    @Override
    public TemplateBlock setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateBlock) super.setContainerItem(itemType);
    }
}
