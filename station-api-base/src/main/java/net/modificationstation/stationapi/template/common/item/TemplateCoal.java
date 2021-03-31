package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TemplateCoal extends net.minecraft.item.Coal implements IItemTemplate<TemplateCoal> {
    
    public TemplateCoal(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateCoal(int i) {
        super(i);
    }

    @Override
    public TemplateCoal setTexturePosition(int texturePosition) {
        return (TemplateCoal) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateCoal setMaxStackSize(int newMaxStackSize) {
        return (TemplateCoal) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateCoal setTexturePosition(int x, int y) {
        return (TemplateCoal) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateCoal setHasSubItems(boolean hasSubItems) {
        return (TemplateCoal) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateCoal setDurability(int durability) {
        return (TemplateCoal) super.setDurability(durability);
    }

    @Override
    public TemplateCoal setRendered3d() {
        return (TemplateCoal) super.setRendered3d();
    }

    @Override
    public TemplateCoal setTranslationKey(String newName) {
        return (TemplateCoal) super.setTranslationKey(newName);
    }

    @Override
    public TemplateCoal setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateCoal) super.setContainerItem(itemType);
    }
}
