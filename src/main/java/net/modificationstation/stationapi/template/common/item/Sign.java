package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Sign extends net.minecraft.item.Sign implements IItemTemplate<Sign> {
    
    public Sign(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Sign(int i) {
        super(i);
    }

    @Override
    public Sign setTexturePosition(int texturePosition) {
        return (Sign) super.setTexturePosition(texturePosition);
    }

    @Override
    public Sign setMaxStackSize(int newMaxStackSize) {
        return (Sign) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Sign setTexturePosition(int x, int y) {
        return (Sign) super.setTexturePosition(x, y);
    }

    @Override
    public Sign setHasSubItems(boolean hasSubItems) {
        return (Sign) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Sign setDurability(int durability) {
        return (Sign) super.setDurability(durability);
    }

    @Override
    public Sign setRendered3d() {
        return (Sign) super.setRendered3d();
    }

    @Override
    public Sign setTranslationKey(String newName) {
        return (Sign) super.setTranslationKey(newName);
    }

    @Override
    public Sign setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Sign) super.setContainerItem(itemType);
    }
}
