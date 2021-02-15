package net.modificationstation.stationapi.template.common.item;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Door extends net.minecraft.item.Door implements IItemTemplate<Door> {
    
    public Door(Identifier identifier, Material arg) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), arg);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Door(int id, Material arg) {
        super(id, arg);
    }

    @Override
    public Door setTexturePosition(int texturePosition) {
        return (Door) super.setTexturePosition(texturePosition);
    }

    @Override
    public Door setMaxStackSize(int newMaxStackSize) {
        return (Door) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Door setTexturePosition(int x, int y) {
        return (Door) super.setTexturePosition(x, y);
    }

    @Override
    public Door setHasSubItems(boolean hasSubItems) {
        return (Door) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Door setDurability(int durability) {
        return (Door) super.setDurability(durability);
    }

    @Override
    public Door setRendered3d() {
        return (Door) super.setRendered3d();
    }

    @Override
    public Door setTranslationKey(String newName) {
        return (Door) super.setTranslationKey(newName);
    }

    @Override
    public Door setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Door) super.setContainerItem(itemType);
    }
}
