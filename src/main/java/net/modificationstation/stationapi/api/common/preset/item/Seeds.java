package net.modificationstation.stationapi.api.common.preset.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Seeds extends net.minecraft.item.Seeds {

    public Seeds(Identifier identifier, int j) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), j);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Seeds(int id, int j) {
        super(id, j);
    }

    @Override
    public Seeds setTexturePosition(int texturePosition) {
        return (Seeds) super.setTexturePosition(texturePosition);
    }

    @Override
    public Seeds setMaxStackSize(int newMaxStackSize) {
        return (Seeds) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Seeds setTexturePosition(int x, int y) {
        return (Seeds) super.setTexturePosition(x, y);
    }

    @Override
    public Seeds setHasSubItems(boolean hasSubItems) {
        return (Seeds) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Seeds setDurability(int durability) {
        return (Seeds) super.setDurability(durability);
    }

    @Override
    public Seeds setRendered3d() {
        return (Seeds) super.setRendered3d();
    }

    @Override
    public Seeds setName(String newName) {
        return (Seeds) super.setName(newName);
    }

    @Override
    public Seeds setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Seeds) super.setContainerItem(itemType);
    }
}
