package net.modificationstation.stationapi.api.template.item;

public class TemplatePiston extends net.minecraft.item.Piston implements ItemTemplate<TemplatePiston> {
    
    public TemplatePiston(int i) {
        super(i);
    }

    @Override
    public TemplatePiston setTexturePosition(int texturePosition) {
        return (TemplatePiston) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplatePiston setMaxStackSize(int newMaxStackSize) {
        return (TemplatePiston) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplatePiston setTexturePosition(int x, int y) {
        return (TemplatePiston) super.setTexturePosition(x, y);
    }

    @Override
    public TemplatePiston setHasSubItems(boolean hasSubItems) {
        return (TemplatePiston) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplatePiston setDurability(int durability) {
        return (TemplatePiston) super.setDurability(durability);
    }

    @Override
    public TemplatePiston setRendered3d() {
        return (TemplatePiston) super.setRendered3d();
    }

    @Override
    public TemplatePiston setTranslationKey(String newName) {
        return (TemplatePiston) super.setTranslationKey(newName);
    }

    @Override
    public TemplatePiston setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplatePiston) super.setContainerItem(itemType);
    }
}
