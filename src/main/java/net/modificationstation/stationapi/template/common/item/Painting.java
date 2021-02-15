package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Painting extends net.minecraft.item.Painting implements IItemTemplate<Painting> {
    
    public Painting(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Painting(int i) {
        super(i);
    }

    @Override
    public Painting setTexturePosition(int texturePosition) {
        return (Painting) super.setTexturePosition(texturePosition);
    }

    @Override
    public Painting setMaxStackSize(int newMaxStackSize) {
        return (Painting) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Painting setTexturePosition(int x, int y) {
        return (Painting) super.setTexturePosition(x, y);
    }

    @Override
    public Painting setHasSubItems(boolean hasSubItems) {
        return (Painting) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Painting setDurability(int durability) {
        return (Painting) super.setDurability(durability);
    }

    @Override
    public Painting setRendered3d() {
        return (Painting) super.setRendered3d();
    }

    @Override
    public Painting setTranslationKey(String newName) {
        return (Painting) super.setTranslationKey(newName);
    }

    @Override
    public Painting setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Painting) super.setContainerItem(itemType);
    }
}
