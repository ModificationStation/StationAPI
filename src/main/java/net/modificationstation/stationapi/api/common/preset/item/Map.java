package net.modificationstation.stationapi.api.common.preset.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Map extends net.minecraft.item.Map {

    public Map(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Map(int i) {
        super(i);
    }

    @Override
    public Map setTexturePosition(int texturePosition) {
        return (Map) super.setTexturePosition(texturePosition);
    }

    @Override
    public Map setMaxStackSize(int newMaxStackSize) {
        return (Map) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Map setTexturePosition(int x, int y) {
        return (Map) super.setTexturePosition(x, y);
    }

    @Override
    public Map setHasSubItems(boolean hasSubItems) {
        return (Map) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Map setDurability(int durability) {
        return (Map) super.setDurability(durability);
    }

    @Override
    public Map setRendered3d() {
        return (Map) super.setRendered3d();
    }

    @Override
    public Map setName(String newName) {
        return (Map) super.setName(newName);
    }

    @Override
    public Map setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Map) super.setContainerItem(itemType);
    }
}
