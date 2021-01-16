package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Bed extends net.minecraft.item.Bed {

    public Bed(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Bed(int i) {
        super(i);
    }

    @Override
    public Bed setTexturePosition(int texturePosition) {
        return (Bed) super.setTexturePosition(texturePosition);
    }

    @Override
    public Bed setMaxStackSize(int newMaxStackSize) {
        return (Bed) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Bed setTexturePosition(int x, int y) {
        return (Bed) super.setTexturePosition(x, y);
    }

    @Override
    public Bed setHasSubItems(boolean hasSubItems) {
        return (Bed) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Bed setDurability(int durability) {
        return (Bed) super.setDurability(durability);
    }

    @Override
    public Bed setRendered3d() {
        return (Bed) super.setRendered3d();
    }

    @Override
    public Bed setName(String newName) {
        return (Bed) super.setName(newName);
    }

    @Override
    public Bed setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Bed) super.setContainerItem(itemType);
    }
}
