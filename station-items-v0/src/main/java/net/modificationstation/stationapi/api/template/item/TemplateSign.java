package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateSign extends net.minecraft.item.Sign implements ItemTemplate<TemplateSign> {
    
    public TemplateSign(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerialID());
        ItemRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateSign(int i) {
        super(i);
    }

    @Override
    public TemplateSign setTexturePosition(int texturePosition) {
        return (TemplateSign) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateSign setMaxStackSize(int newMaxStackSize) {
        return (TemplateSign) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateSign setTexturePosition(int x, int y) {
        return (TemplateSign) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateSign setHasSubItems(boolean hasSubItems) {
        return (TemplateSign) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateSign setDurability(int durability) {
        return (TemplateSign) super.setDurability(durability);
    }

    @Override
    public TemplateSign setRendered3d() {
        return (TemplateSign) super.setRendered3d();
    }

    @Override
    public TemplateSign setTranslationKey(String newName) {
        return (TemplateSign) super.setTranslationKey(newName);
    }

    @Override
    public TemplateSign setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateSign) super.setContainerItem(itemType);
    }
}
