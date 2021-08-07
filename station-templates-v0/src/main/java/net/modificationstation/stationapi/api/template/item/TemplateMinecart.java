package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateMinecart extends net.minecraft.item.Minecart implements ItemTemplate<TemplateMinecart> {
    
    public TemplateMinecart(Identifier identifier, int j) {
        this(ItemRegistry.INSTANCE.getNextSerialIDShifted(), j);
        ItemRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateMinecart(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateMinecart setTexturePosition(int texturePosition) {
        return (TemplateMinecart) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateMinecart setMaxStackSize(int newMaxStackSize) {
        return (TemplateMinecart) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateMinecart setTexturePosition(int x, int y) {
        return (TemplateMinecart) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateMinecart setHasSubItems(boolean hasSubItems) {
        return (TemplateMinecart) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateMinecart setDurability(int durability) {
        return (TemplateMinecart) super.setDurability(durability);
    }

    @Override
    public TemplateMinecart setRendered3d() {
        return (TemplateMinecart) super.setRendered3d();
    }

    @Override
    public TemplateMinecart setTranslationKey(String newName) {
        return (TemplateMinecart) super.setTranslationKey(newName);
    }

    @Override
    public TemplateMinecart setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateMinecart) super.setContainerItem(itemType);
    }

    @Override
    public Atlas getAtlas() {
        return ItemTemplate.super.getAtlas();
    }
}
