package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateCoal extends net.minecraft.item.Coal implements ItemTemplate<TemplateCoal> {
    
    public TemplateCoal(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerialID());
        ItemRegistry.INSTANCE.register(identifier, this);
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
