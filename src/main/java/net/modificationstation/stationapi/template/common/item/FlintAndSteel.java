package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class FlintAndSteel extends net.minecraft.item.FlintAndSteel {

    public FlintAndSteel(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public FlintAndSteel(int i) {
        super(i);
    }

    @Override
    public FlintAndSteel setTexturePosition(int texturePosition) {
        return (FlintAndSteel) super.setTexturePosition(texturePosition);
    }

    @Override
    public FlintAndSteel setMaxStackSize(int newMaxStackSize) {
        return (FlintAndSteel) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public FlintAndSteel setTexturePosition(int x, int y) {
        return (FlintAndSteel) super.setTexturePosition(x, y);
    }

    @Override
    public FlintAndSteel setHasSubItems(boolean hasSubItems) {
        return (FlintAndSteel) super.setHasSubItems(hasSubItems);
    }

    @Override
    public FlintAndSteel setDurability(int durability) {
        return (FlintAndSteel) super.setDurability(durability);
    }

    @Override
    public FlintAndSteel setRendered3d() {
        return (FlintAndSteel) super.setRendered3d();
    }

    @Override
    public FlintAndSteel setName(String newName) {
        return (FlintAndSteel) super.setTranslationKey(newName);
    }

    @Override
    public FlintAndSteel setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (FlintAndSteel) super.setContainerItem(itemType);
    }
}
