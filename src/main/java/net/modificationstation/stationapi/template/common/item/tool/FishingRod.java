package net.modificationstation.stationapi.template.common.item.tool;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.template.common.item.IItemTemplate;

public class FishingRod extends net.minecraft.item.tool.FishingRod implements IItemTemplate<FishingRod> {
    
    public FishingRod(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public FishingRod(int i) {
        super(i);
    }

    @Override
    public FishingRod setTexturePosition(int texturePosition) {
        return (FishingRod) super.setTexturePosition(texturePosition);
    }

    @Override
    public FishingRod setMaxStackSize(int newMaxStackSize) {
        return (FishingRod) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public FishingRod setTexturePosition(int x, int y) {
        return (FishingRod) super.setTexturePosition(x, y);
    }

    @Override
    public FishingRod setHasSubItems(boolean hasSubItems) {
        return (FishingRod) super.setHasSubItems(hasSubItems);
    }

    @Override
    public FishingRod setDurability(int durability) {
        return (FishingRod) super.setDurability(durability);
    }

    @Override
    public FishingRod setRendered3d() {
        return (FishingRod) super.setRendered3d();
    }

    @Override
    public FishingRod setTranslationKey(String newName) {
        return (FishingRod) super.setTranslationKey(newName);
    }

    @Override
    public FishingRod setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (FishingRod) super.setContainerItem(itemType);
    }
}
