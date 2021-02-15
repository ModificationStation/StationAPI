package net.modificationstation.stationapi.template.common.item.tool;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.template.common.item.IItemTemplate;

public class Bucket extends net.minecraft.item.tool.Bucket implements IItemTemplate<Bucket> {
    
    public Bucket(Identifier identifier, int j) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), j);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Bucket(int i, int j) {
        super(i, j);
    }

    @Override
    public Bucket setTexturePosition(int texturePosition) {
        return (Bucket) super.setTexturePosition(texturePosition);
    }

    @Override
    public Bucket setMaxStackSize(int newMaxStackSize) {
        return (Bucket) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Bucket setTexturePosition(int x, int y) {
        return (Bucket) super.setTexturePosition(x, y);
    }

    @Override
    public Bucket setHasSubItems(boolean hasSubItems) {
        return (Bucket) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Bucket setDurability(int durability) {
        return (Bucket) super.setDurability(durability);
    }

    @Override
    public Bucket setRendered3d() {
        return (Bucket) super.setRendered3d();
    }

    @Override
    public Bucket setTranslationKey(String newName) {
        return (Bucket) super.setTranslationKey(newName);
    }

    @Override
    public Bucket setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Bucket) super.setContainerItem(itemType);
    }
}
