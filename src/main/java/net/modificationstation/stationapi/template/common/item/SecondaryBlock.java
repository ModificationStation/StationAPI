package net.modificationstation.stationapi.template.common.item;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class SecondaryBlock extends net.minecraft.item.SecondaryBlock {

    public SecondaryBlock(Identifier identifier, BlockBase tile) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), tile);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public SecondaryBlock(int id, BlockBase tile) {
        super(id, tile);
    }

    @Override
    public SecondaryBlock setTexturePosition(int texturePosition) {
        return (SecondaryBlock) super.setTexturePosition(texturePosition);
    }

    @Override
    public SecondaryBlock setMaxStackSize(int newMaxStackSize) {
        return (SecondaryBlock) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public SecondaryBlock setTexturePosition(int x, int y) {
        return (SecondaryBlock) super.setTexturePosition(x, y);
    }

    @Override
    public SecondaryBlock setHasSubItems(boolean hasSubItems) {
        return (SecondaryBlock) super.setHasSubItems(hasSubItems);
    }

    @Override
    public SecondaryBlock setDurability(int durability) {
        return (SecondaryBlock) super.setDurability(durability);
    }

    @Override
    public SecondaryBlock setRendered3d() {
        return (SecondaryBlock) super.setRendered3d();
    }

    @Override
    public SecondaryBlock setName(String newName) {
        return (SecondaryBlock) super.setTranslationKey(newName);
    }

    @Override
    public SecondaryBlock setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (SecondaryBlock) super.setContainerItem(itemType);
    }
}
