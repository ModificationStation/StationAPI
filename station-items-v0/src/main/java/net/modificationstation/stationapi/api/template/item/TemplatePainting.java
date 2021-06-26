package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplatePainting extends net.minecraft.item.Painting implements ItemTemplate<TemplatePainting> {
    
    public TemplatePainting(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerialIDShifted());
        ItemRegistry.INSTANCE.register(identifier, this);
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
