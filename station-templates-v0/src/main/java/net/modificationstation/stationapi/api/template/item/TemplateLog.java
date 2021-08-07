package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;

public class TemplateLog extends net.minecraft.item.Log implements ItemTemplate<TemplateLog> {
    
    public TemplateLog(int i) {
        super(i);
    }

    @Override
    public TemplateLog setTexturePosition(int texturePosition) {
        return (TemplateLog) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateLog setMaxStackSize(int newMaxStackSize) {
        return (TemplateLog) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateLog setTexturePosition(int x, int y) {
        return (TemplateLog) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateLog setHasSubItems(boolean hasSubItems) {
        return (TemplateLog) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateLog setDurability(int durability) {
        return (TemplateLog) super.setDurability(durability);
    }

    @Override
    public TemplateLog setRendered3d() {
        return (TemplateLog) super.setRendered3d();
    }

    @Override
    public TemplateLog setTranslationKey(String newName) {
        return (TemplateLog) super.setTranslationKey(newName);
    }

    @Override
    public TemplateLog setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateLog) super.setContainerItem(itemType);
    }

    @Override
    public Atlas getAtlas() {
        return ItemTemplate.super.getAtlas();
    }
}
