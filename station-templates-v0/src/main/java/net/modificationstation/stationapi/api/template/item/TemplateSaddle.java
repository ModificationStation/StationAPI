package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateSaddle extends net.minecraft.item.Saddle implements ItemTemplate<TemplateSaddle> {
    
    public TemplateSaddle(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSaddle(int i) {
        super(i);
    }

    @Override
    public TemplateSaddle setTexturePosition(int texturePosition) {
        return (TemplateSaddle) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateSaddle setMaxStackSize(int newMaxStackSize) {
        return (TemplateSaddle) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateSaddle setTexturePosition(int x, int y) {
        return (TemplateSaddle) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateSaddle setHasSubItems(boolean hasSubItems) {
        return (TemplateSaddle) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateSaddle setDurability(int durability) {
        return (TemplateSaddle) super.setDurability(durability);
    }

    @Override
    public TemplateSaddle setRendered3d() {
        return (TemplateSaddle) super.setRendered3d();
    }

    @Override
    public TemplateSaddle setTranslationKey(String newName) {
        return (TemplateSaddle) super.setTranslationKey(newName);
    }

    @Override
    public TemplateSaddle setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateSaddle) super.setContainerItem(itemType);
    }
}
