package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateBoat extends net.minecraft.item.Boat implements ItemTemplate<TemplateBoat> {
    
    public TemplateBoat(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerialID());
        ItemRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateBoat(int i) {
        super(i);
    }

    @Override
    public TemplateBoat setTexturePosition(int texturePosition) {
        return (TemplateBoat) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateBoat setMaxStackSize(int newMaxStackSize) {
        return (TemplateBoat) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateBoat setTexturePosition(int x, int y) {
        return (TemplateBoat) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateBoat setHasSubItems(boolean hasSubItems) {
        return (TemplateBoat) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateBoat setDurability(int durability) {
        return (TemplateBoat) super.setDurability(durability);
    }

    @Override
    public TemplateBoat setRendered3d() {
        return (TemplateBoat) super.setRendered3d();
    }

    @Override
    public TemplateBoat setTranslationKey(String newName) {
        return (TemplateBoat) super.setTranslationKey(newName);
    }

    @Override
    public TemplateBoat setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateBoat) super.setContainerItem(itemType);
    }
}
