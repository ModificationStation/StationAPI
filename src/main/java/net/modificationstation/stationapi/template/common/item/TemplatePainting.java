package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TemplatePainting extends net.minecraft.item.Painting implements IItemTemplate<TemplatePainting> {
    
    public TemplatePainting(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplatePainting(int i) {
        super(i);
    }

    @Override
    public TemplatePainting setTexturePosition(int texturePosition) {
        return (TemplatePainting) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplatePainting setMaxStackSize(int newMaxStackSize) {
        return (TemplatePainting) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplatePainting setTexturePosition(int x, int y) {
        return (TemplatePainting) super.setTexturePosition(x, y);
    }

    @Override
    public TemplatePainting setHasSubItems(boolean hasSubItems) {
        return (TemplatePainting) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplatePainting setDurability(int durability) {
        return (TemplatePainting) super.setDurability(durability);
    }

    @Override
    public TemplatePainting setRendered3d() {
        return (TemplatePainting) super.setRendered3d();
    }

    @Override
    public TemplatePainting setTranslationKey(String newName) {
        return (TemplatePainting) super.setTranslationKey(newName);
    }

    @Override
    public TemplatePainting setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplatePainting) super.setContainerItem(itemType);
    }
}
