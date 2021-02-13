package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Dye extends net.minecraft.item.Dye {
    
    public Dye(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Dye(int i) {
        super(i);
    }

    @Override
    public Dye setTexturePosition(int texturePosition) {
        return (Dye) super.setTexturePosition(texturePosition);
    }

    @Override
    public Dye setMaxStackSize(int newMaxStackSize) {
        return (Dye) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Dye setTexturePosition(int x, int y) {
        return (Dye) super.setTexturePosition(x, y);
    }

    @Override
    public Dye setHasSubItems(boolean hasSubItems) {
        return (Dye) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Dye setDurability(int durability) {
        return (Dye) super.setDurability(durability);
    }

    @Override
    public Dye setRendered3d() {
        return (Dye) super.setRendered3d();
    }

    @Override
    public Dye setName(String newName) {
        return (Dye) super.setTranslationKey(newName);
    }

    @Override
    public Dye setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Dye) super.setContainerItem(itemType);
    }
}
