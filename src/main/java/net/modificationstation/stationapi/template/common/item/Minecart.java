package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Minecart extends net.minecraft.item.Minecart {
    
    public Minecart(Identifier identifier, int j) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), j);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Minecart(int i, int j) {
        super(i, j);
    }

    @Override
    public Minecart setTexturePosition(int texturePosition) {
        return (Minecart) super.setTexturePosition(texturePosition);
    }

    @Override
    public Minecart setMaxStackSize(int newMaxStackSize) {
        return (Minecart) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Minecart setTexturePosition(int x, int y) {
        return (Minecart) super.setTexturePosition(x, y);
    }

    @Override
    public Minecart setHasSubItems(boolean hasSubItems) {
        return (Minecart) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Minecart setDurability(int durability) {
        return (Minecart) super.setDurability(durability);
    }

    @Override
    public Minecart setRendered3d() {
        return (Minecart) super.setRendered3d();
    }

    @Override
    public Minecart setTranslationKey(String newName) {
        return (Minecart) super.setTranslationKey(newName);
    }

    @Override
    public Minecart setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Minecart) super.setContainerItem(itemType);
    }
}
