package net.modificationstation.stationapi.api.template.item;

public class TemplateWool extends net.minecraft.item.Wool implements IItemTemplate<TemplateWool> {

    public TemplateWool(int i) {
        super(i);
    }

    @Override
    public TemplateWool setTexturePosition(int texturePosition) {
        return (TemplateWool) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateWool setMaxStackSize(int newMaxStackSize) {
        return (TemplateWool) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateWool setTexturePosition(int x, int y) {
        return (TemplateWool) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateWool setHasSubItems(boolean hasSubItems) {
        return (TemplateWool) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateWool setDurability(int durability) {
        return (TemplateWool) super.setDurability(durability);
    }

    @Override
    public TemplateWool setRendered3d() {
        return (TemplateWool) super.setRendered3d();
    }

    @Override
    public TemplateWool setTranslationKey(String newName) {
        return (TemplateWool) super.setTranslationKey(newName);
    }

    @Override
    public TemplateWool setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateWool) super.setContainerItem(itemType);
    }
}
