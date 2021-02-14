package net.modificationstation.stationapi.template.common.item.armour;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Armour extends net.minecraft.item.armour.Armour {

    public Armour(Identifier identifier, int j, int k, int slot) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), j, k, slot);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Armour(int id, int j, int k, int slot) {
        super(id, j, k, slot);
    }

    @Override
    public Armour setTexturePosition(int texturePosition) {
        return (Armour) super.setTexturePosition(texturePosition);
    }

    @Override
    public Armour setMaxStackSize(int newMaxStackSize) {
        return (Armour) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Armour setTexturePosition(int x, int y) {
        return (Armour) super.setTexturePosition(x, y);
    }

    @Override
    public Armour setHasSubItems(boolean hasSubItems) {
        return (Armour) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Armour setDurability(int durability) {
        return (Armour) super.setDurability(durability);
    }

    @Override
    public Armour setRendered3d() {
        return (Armour) super.setRendered3d();
    }

    @Override
    public Armour setTranslationKey(String newName) {
        return (Armour) super.setTranslationKey(newName);
    }

    @Override
    public Armour setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Armour) super.setContainerItem(itemType);
    }
}
