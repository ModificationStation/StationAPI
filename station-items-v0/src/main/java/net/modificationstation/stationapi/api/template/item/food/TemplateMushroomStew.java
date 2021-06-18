package net.modificationstation.stationapi.api.template.item.food;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateMushroomStew extends net.minecraft.item.food.MushroomStew implements ItemTemplate<TemplateMushroomStew> {
    
    public TemplateMushroomStew(Identifier identifier, int healAmount) {
        this(ItemRegistry.INSTANCE.getNextSerialID(), healAmount);
        ItemRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateMushroomStew(int id, int healAmount) {
        super(id, healAmount);
    }

    @Override
    public TemplateMushroomStew setTexturePosition(int texturePosition) {
        return (TemplateMushroomStew) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateMushroomStew setMaxStackSize(int newMaxStackSize) {
        return (TemplateMushroomStew) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateMushroomStew setTexturePosition(int x, int y) {
        return (TemplateMushroomStew) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateMushroomStew setHasSubItems(boolean hasSubItems) {
        return (TemplateMushroomStew) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateMushroomStew setDurability(int durability) {
        return (TemplateMushroomStew) super.setDurability(durability);
    }

    @Override
    public TemplateMushroomStew setRendered3d() {
        return (TemplateMushroomStew) super.setRendered3d();
    }

    @Override
    public TemplateMushroomStew setTranslationKey(String newName) {
        return (TemplateMushroomStew) super.setTranslationKey(newName);
    }

    @Override
    public TemplateMushroomStew setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateMushroomStew) super.setContainerItem(itemType);
    }
}
