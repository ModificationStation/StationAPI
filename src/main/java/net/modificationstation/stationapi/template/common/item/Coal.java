package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Coal extends net.minecraft.item.Coal {
    
    public Coal(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Coal(int i) {
        super(i);
    }

    @Override
    public Coal setTexturePosition(int texturePosition) {
        return (Coal) super.setTexturePosition(texturePosition);
    }

    @Override
    public Coal setMaxStackSize(int newMaxStackSize) {
        return (Coal) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Coal setTexturePosition(int x, int y) {
        return (Coal) super.setTexturePosition(x, y);
    }

    @Override
    public Coal setHasSubItems(boolean hasSubItems) {
        return (Coal) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Coal setDurability(int durability) {
        return (Coal) super.setDurability(durability);
    }

    @Override
    public Coal setRendered3d() {
        return (Coal) super.setRendered3d();
    }

    @Override
    public Coal setName(String newName) {
        return (Coal) super.setTranslationKey(newName);
    }

    @Override
    public Coal setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Coal) super.setContainerItem(itemType);
    }
}
