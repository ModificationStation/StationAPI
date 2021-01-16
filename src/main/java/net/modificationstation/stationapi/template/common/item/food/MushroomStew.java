package net.modificationstation.stationapi.template.common.item.food;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class MushroomStew extends net.minecraft.item.food.MushroomStew {
    
    public MushroomStew(Identifier identifier, int healAmount) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), healAmount);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public MushroomStew(int id, int healAmount) {
        super(id, healAmount);
    }

    @Override
    public MushroomStew setTexturePosition(int texturePosition) {
        return (MushroomStew) super.setTexturePosition(texturePosition);
    }

    @Override
    public MushroomStew setMaxStackSize(int newMaxStackSize) {
        return (MushroomStew) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public MushroomStew setTexturePosition(int x, int y) {
        return (MushroomStew) super.setTexturePosition(x, y);
    }

    @Override
    public MushroomStew setHasSubItems(boolean hasSubItems) {
        return (MushroomStew) super.setHasSubItems(hasSubItems);
    }

    @Override
    public MushroomStew setDurability(int durability) {
        return (MushroomStew) super.setDurability(durability);
    }

    @Override
    public MushroomStew setRendered3d() {
        return (MushroomStew) super.setRendered3d();
    }

    @Override
    public MushroomStew setName(String newName) {
        return (MushroomStew) super.setName(newName);
    }

    @Override
    public MushroomStew setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (MushroomStew) super.setContainerItem(itemType);
    }
}
