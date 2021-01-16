package net.modificationstation.stationapi.template.common.item.food;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Cookie extends net.minecraft.item.food.Cookie {
    
    public Cookie(Identifier identifier, int healAmount, boolean isWolfFood, int maxStackSize) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), healAmount, isWolfFood, maxStackSize);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Cookie(int id, int healAmount, boolean isWolfFood, int maxStackSize) {
        super(id, healAmount, isWolfFood, maxStackSize);
    }

    @Override
    public Cookie setTexturePosition(int texturePosition) {
        return (Cookie) super.setTexturePosition(texturePosition);
    }

    @Override
    public Cookie setMaxStackSize(int newMaxStackSize) {
        return (Cookie) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Cookie setTexturePosition(int x, int y) {
        return (Cookie) super.setTexturePosition(x, y);
    }

    @Override
    public Cookie setHasSubItems(boolean hasSubItems) {
        return (Cookie) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Cookie setDurability(int durability) {
        return (Cookie) super.setDurability(durability);
    }

    @Override
    public Cookie setRendered3d() {
        return (Cookie) super.setRendered3d();
    }

    @Override
    public Cookie setName(String newName) {
        return (Cookie) super.setName(newName);
    }

    @Override
    public Cookie setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Cookie) super.setContainerItem(itemType);
    }
}
