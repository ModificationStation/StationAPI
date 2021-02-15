package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Egg extends net.minecraft.item.Egg implements IItemTemplate<Egg> {

    public Egg(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Egg(int i) {
        super(i);
    }

    @Override
    public Egg setTexturePosition(int texturePosition) {
        return (Egg) super.setTexturePosition(texturePosition);
    }

    @Override
    public Egg setMaxStackSize(int newMaxStackSize) {
        return (Egg) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Egg setTexturePosition(int x, int y) {
        return (Egg) super.setTexturePosition(x, y);
    }

    @Override
    public Egg setHasSubItems(boolean hasSubItems) {
        return (Egg) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Egg setDurability(int durability) {
        return (Egg) super.setDurability(durability);
    }

    @Override
    public Egg setRendered3d() {
        return (Egg) super.setRendered3d();
    }

    @Override
    public Egg setTranslationKey(String newName) {
        return (Egg) super.setTranslationKey(newName);
    }

    @Override
    public Egg setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Egg) super.setContainerItem(itemType);
    }
}
