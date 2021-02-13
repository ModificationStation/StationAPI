package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Boat extends net.minecraft.item.Boat {
    
    public Boat(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Boat(int i) {
        super(i);
    }

    @Override
    public Boat setTexturePosition(int texturePosition) {
        return (Boat) super.setTexturePosition(texturePosition);
    }

    @Override
    public Boat setMaxStackSize(int newMaxStackSize) {
        return (Boat) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Boat setTexturePosition(int x, int y) {
        return (Boat) super.setTexturePosition(x, y);
    }

    @Override
    public Boat setHasSubItems(boolean hasSubItems) {
        return (Boat) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Boat setDurability(int durability) {
        return (Boat) super.setDurability(durability);
    }

    @Override
    public Boat setRendered3d() {
        return (Boat) super.setRendered3d();
    }

    @Override
    public Boat setName(String newName) {
        return (Boat) super.setTranslationKey(newName);
    }

    @Override
    public Boat setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Boat) super.setContainerItem(itemType);
    }
}
