package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateMap extends net.minecraft.item.Map implements ItemTemplate<TemplateMap> {

    public TemplateMap(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerialIDShifted());
        ItemRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateMap(int i) {
        super(i);
    }

    @Override
    public TemplateMap setTexturePosition(int texturePosition) {
        return (TemplateMap) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateMap setMaxStackSize(int newMaxStackSize) {
        return (TemplateMap) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateMap setTexturePosition(int x, int y) {
        return (TemplateMap) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateMap setHasSubItems(boolean hasSubItems) {
        return (TemplateMap) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateMap setDurability(int durability) {
        return (TemplateMap) super.setDurability(durability);
    }

    @Override
    public TemplateMap setRendered3d() {
        return (TemplateMap) super.setRendered3d();
    }

    @Override
    public TemplateMap setTranslationKey(String newName) {
        return (TemplateMap) super.setTranslationKey(newName);
    }

    @Override
    public TemplateMap setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateMap) super.setContainerItem(itemType);
    }

    @Override
    public Atlas getAtlas() {
        return ItemTemplate.super.getAtlas();
    }
}
