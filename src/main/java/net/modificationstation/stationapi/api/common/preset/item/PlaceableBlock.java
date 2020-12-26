package net.modificationstation.stationapi.api.common.preset.item;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class PlaceableBlock extends net.minecraft.item.PlaceableBlock {

    public PlaceableBlock(Identifier identifier, BlockBase tile) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), tile);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public PlaceableBlock(int id, BlockBase tile) {
        super(id, tile);
    }

    @Override
    public PlaceableBlock setTexturePosition(int texturePosition) {
        return (PlaceableBlock) super.setTexturePosition(texturePosition);
    }

    @Override
    public PlaceableBlock setMaxStackSize(int newMaxStackSize) {
        return (PlaceableBlock) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public PlaceableBlock setTexturePosition(int x, int y) {
        return (PlaceableBlock) super.setTexturePosition(x, y);
    }

    @Override
    public PlaceableBlock setHasSubItems(boolean hasSubItems) {
        return (PlaceableBlock) super.setHasSubItems(hasSubItems);
    }

    @Override
    public PlaceableBlock setDurability(int durability) {
        return (PlaceableBlock) super.setDurability(durability);
    }

    @Override
    public PlaceableBlock setRendered3d() {
        return (PlaceableBlock) super.setRendered3d();
    }

    @Override
    public PlaceableBlock setName(String newName) {
        return (PlaceableBlock) super.setName(newName);
    }

    @Override
    public PlaceableBlock setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (PlaceableBlock) super.setContainerItem(itemType);
    }
}
