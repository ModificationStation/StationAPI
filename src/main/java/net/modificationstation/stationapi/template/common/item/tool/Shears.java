package net.modificationstation.stationapi.template.common.item.tool;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Shears extends net.minecraft.item.tool.Shears {
    
    public Shears(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Shears(int i) {
        super(i);
    }

    @Override
    public Shears setTexturePosition(int texturePosition) {
        return (Shears) super.setTexturePosition(texturePosition);
    }

    @Override
    public Shears setMaxStackSize(int newMaxStackSize) {
        return (Shears) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Shears setTexturePosition(int x, int y) {
        return (Shears) super.setTexturePosition(x, y);
    }

    @Override
    public Shears setHasSubItems(boolean hasSubItems) {
        return (Shears) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Shears setDurability(int durability) {
        return (Shears) super.setDurability(durability);
    }

    @Override
    public Shears setRendered3d() {
        return (Shears) super.setRendered3d();
    }

    @Override
    public Shears setName(String newName) {
        return (Shears) super.setTranslationKey(newName);
    }

    @Override
    public Shears setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Shears) super.setContainerItem(itemType);
    }
}
