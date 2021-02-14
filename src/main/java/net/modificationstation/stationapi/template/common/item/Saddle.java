package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Saddle extends net.minecraft.item.Saddle {
    
    public Saddle(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Saddle(int i) {
        super(i);
    }

    @Override
    public Saddle setTexturePosition(int texturePosition) {
        return (Saddle) super.setTexturePosition(texturePosition);
    }

    @Override
    public Saddle setMaxStackSize(int newMaxStackSize) {
        return (Saddle) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Saddle setTexturePosition(int x, int y) {
        return (Saddle) super.setTexturePosition(x, y);
    }

    @Override
    public Saddle setHasSubItems(boolean hasSubItems) {
        return (Saddle) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Saddle setDurability(int durability) {
        return (Saddle) super.setDurability(durability);
    }

    @Override
    public Saddle setRendered3d() {
        return (Saddle) super.setRendered3d();
    }

    @Override
    public Saddle setTranslationKey(String newName) {
        return (Saddle) super.setTranslationKey(newName);
    }

    @Override
    public Saddle setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Saddle) super.setContainerItem(itemType);
    }
}
