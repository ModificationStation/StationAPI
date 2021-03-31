package net.modificationstation.stationapi.template.common.item;

public class TemplateLeaves extends net.minecraft.item.Leaves implements IItemTemplate<TemplateLeaves> {
    
    public TemplateLeaves(int i) {
        super(i);
    }

    @Override
    public TemplateLeaves setTexturePosition(int texturePosition) {
        return (TemplateLeaves) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateLeaves setMaxStackSize(int newMaxStackSize) {
        return (TemplateLeaves) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateLeaves setTexturePosition(int x, int y) {
        return (TemplateLeaves) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateLeaves setHasSubItems(boolean hasSubItems) {
        return (TemplateLeaves) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateLeaves setDurability(int durability) {
        return (TemplateLeaves) super.setDurability(durability);
    }

    @Override
    public TemplateLeaves setRendered3d() {
        return (TemplateLeaves) super.setRendered3d();
    }

    @Override
    public TemplateLeaves setTranslationKey(String newName) {
        return (TemplateLeaves) super.setTranslationKey(newName);
    }

    @Override
    public TemplateLeaves setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateLeaves) super.setContainerItem(itemType);
    }
}
