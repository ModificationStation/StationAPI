package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Snowball extends net.minecraft.item.Snowball implements IItemTemplate<Snowball> {

    public Snowball(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Snowball(int i) {
        super(i);
    }

    @Override
    public Snowball setTexturePosition(int texturePosition) {
        return (Snowball) super.setTexturePosition(texturePosition);
    }

    @Override
    public Snowball setMaxStackSize(int newMaxStackSize) {
        return (Snowball) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Snowball setTexturePosition(int x, int y) {
        return (Snowball) super.setTexturePosition(x, y);
    }

    @Override
    public Snowball setHasSubItems(boolean hasSubItems) {
        return (Snowball) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Snowball setDurability(int durability) {
        return (Snowball) super.setDurability(durability);
    }

    @Override
    public Snowball setRendered3d() {
        return (Snowball) super.setRendered3d();
    }

    @Override
    public Snowball setTranslationKey(String newName) {
        return (Snowball) super.setTranslationKey(newName);
    }

    @Override
    public Snowball setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Snowball) super.setContainerItem(itemType);
    }
}
