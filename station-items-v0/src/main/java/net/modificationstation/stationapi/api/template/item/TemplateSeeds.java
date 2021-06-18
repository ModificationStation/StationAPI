package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateSeeds extends net.minecraft.item.Seeds implements ItemTemplate<TemplateSeeds> {

    public TemplateSeeds(Identifier identifier, int j) {
        this(ItemRegistry.INSTANCE.getNextSerialID(), j);
        ItemRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateSeeds(int id, int j) {
        super(id, j);
    }

    @Override
    public TemplateSeeds setTexturePosition(int texturePosition) {
        return (TemplateSeeds) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateSeeds setMaxStackSize(int newMaxStackSize) {
        return (TemplateSeeds) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateSeeds setTexturePosition(int x, int y) {
        return (TemplateSeeds) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateSeeds setHasSubItems(boolean hasSubItems) {
        return (TemplateSeeds) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateSeeds setDurability(int durability) {
        return (TemplateSeeds) super.setDurability(durability);
    }

    @Override
    public TemplateSeeds setRendered3d() {
        return (TemplateSeeds) super.setRendered3d();
    }

    @Override
    public TemplateSeeds setTranslationKey(String newName) {
        return (TemplateSeeds) super.setTranslationKey(newName);
    }

    @Override
    public TemplateSeeds setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateSeeds) super.setContainerItem(itemType);
    }
}
