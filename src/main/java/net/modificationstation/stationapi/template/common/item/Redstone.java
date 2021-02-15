package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Redstone extends net.minecraft.item.Redstone implements IItemTemplate<Redstone> {
    
    public Redstone(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Redstone(int i) {
        super(i);
    }

    @Override
    public Redstone setTexturePosition(int texturePosition) {
        return (Redstone) super.setTexturePosition(texturePosition);
    }

    @Override
    public Redstone setMaxStackSize(int newMaxStackSize) {
        return (Redstone) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Redstone setTexturePosition(int x, int y) {
        return (Redstone) super.setTexturePosition(x, y);
    }

    @Override
    public Redstone setHasSubItems(boolean hasSubItems) {
        return (Redstone) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Redstone setDurability(int durability) {
        return (Redstone) super.setDurability(durability);
    }

    @Override
    public Redstone setRendered3d() {
        return (Redstone) super.setRendered3d();
    }

    @Override
    public Redstone setTranslationKey(String newName) {
        return (Redstone) super.setTranslationKey(newName);
    }

    @Override
    public Redstone setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Redstone) super.setContainerItem(itemType);
    }
}
