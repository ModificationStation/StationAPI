package net.modificationstation.stationapi.template.common.item.tool;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Bow extends net.minecraft.item.tool.Bow {
    
    public Bow(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Bow(int i) {
        super(i);
    }

    @Override
    public Bow setTexturePosition(int texturePosition) {
        return (Bow) super.setTexturePosition(texturePosition);
    }

    @Override
    public Bow setMaxStackSize(int newMaxStackSize) {
        return (Bow) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Bow setTexturePosition(int x, int y) {
        return (Bow) super.setTexturePosition(x, y);
    }

    @Override
    public Bow setHasSubItems(boolean hasSubItems) {
        return (Bow) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Bow setDurability(int durability) {
        return (Bow) super.setDurability(durability);
    }

    @Override
    public Bow setRendered3d() {
        return (Bow) super.setRendered3d();
    }

    @Override
    public Bow setName(String newName) {
        return (Bow) super.setTranslationKey(newName);
    }

    @Override
    public Bow setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Bow) super.setContainerItem(itemType);
    }
}
