package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class MapBase extends net.minecraft.item.MapBase implements IItemTemplate<MapBase> {

    public MapBase(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public MapBase(int i) {
        super(i);
    }

    @Override
    public MapBase setTexturePosition(int texturePosition) {
        return (MapBase) super.setTexturePosition(texturePosition);
    }

    @Override
    public MapBase setMaxStackSize(int newMaxStackSize) {
        return (MapBase) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public MapBase setTexturePosition(int x, int y) {
        return (MapBase) super.setTexturePosition(x, y);
    }

    @Override
    public MapBase setHasSubItems(boolean hasSubItems) {
        return (MapBase) super.setHasSubItems(hasSubItems);
    }

    @Override
    public MapBase setDurability(int durability) {
        return (MapBase) super.setDurability(durability);
    }

    @Override
    public MapBase setRendered3d() {
        return (MapBase) super.setRendered3d();
    }

    @Override
    public MapBase setTranslationKey(String newName) {
        return (MapBase) super.setTranslationKey(newName);
    }

    @Override
    public MapBase setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (MapBase) super.setContainerItem(itemType);
    }
}
