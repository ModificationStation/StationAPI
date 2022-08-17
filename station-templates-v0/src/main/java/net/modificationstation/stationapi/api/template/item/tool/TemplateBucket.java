package net.modificationstation.stationapi.api.template.item.tool;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateBucket extends net.minecraft.item.tool.Bucket implements ItemTemplate<TemplateBucket> {
    
    public TemplateBucket(Identifier identifier, int j) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted(), j);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBucket(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateBucket setTexturePosition(int texturePosition) {
        return (TemplateBucket) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateBucket setMaxStackSize(int newMaxStackSize) {
        return (TemplateBucket) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateBucket setTexturePosition(int x, int y) {
        return (TemplateBucket) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateBucket setHasSubItems(boolean hasSubItems) {
        return (TemplateBucket) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateBucket setDurability(int durability) {
        return (TemplateBucket) super.setDurability(durability);
    }

    @Override
    public TemplateBucket setRendered3d() {
        return (TemplateBucket) super.setRendered3d();
    }

    @Override
    public TemplateBucket setTranslationKey(String newName) {
        return (TemplateBucket) super.setTranslationKey(newName);
    }

    @Override
    public TemplateBucket setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateBucket) super.setContainerItem(itemType);
    }
}
