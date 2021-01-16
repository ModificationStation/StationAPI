package net.modificationstation.stationapi.template.common.item.food;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class FoodBase extends net.minecraft.item.food.FoodBase {
    
    public FoodBase(Identifier identifier, int healAmount, boolean isWolfFood) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), healAmount, isWolfFood);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public FoodBase(int id, int healAmount, boolean isWolfFood) {
        super(id, healAmount, isWolfFood);
    }

    @Override
    public FoodBase setTexturePosition(int texturePosition) {
        return (FoodBase) super.setTexturePosition(texturePosition);
    }

    @Override
    public FoodBase setMaxStackSize(int newMaxStackSize) {
        return (FoodBase) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public FoodBase setTexturePosition(int x, int y) {
        return (FoodBase) super.setTexturePosition(x, y);
    }

    @Override
    public FoodBase setHasSubItems(boolean hasSubItems) {
        return (FoodBase) super.setHasSubItems(hasSubItems);
    }

    @Override
    public FoodBase setDurability(int durability) {
        return (FoodBase) super.setDurability(durability);
    }

    @Override
    public FoodBase setRendered3d() {
        return (FoodBase) super.setRendered3d();
    }

    @Override
    public FoodBase setName(String newName) {
        return (FoodBase) super.setName(newName);
    }

    @Override
    public FoodBase setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (FoodBase) super.setContainerItem(itemType);
    }
}
