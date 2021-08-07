package net.modificationstation.stationapi.api.template.item.tool;

import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateFishingRod extends net.minecraft.item.tool.FishingRod implements ItemTemplate<TemplateFishingRod> {
    
    public TemplateFishingRod(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerialIDShifted());
        ItemRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateFishingRod(int i) {
        super(i);
    }

    @Override
    public TemplateFishingRod setTexturePosition(int texturePosition) {
        return (TemplateFishingRod) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateFishingRod setMaxStackSize(int newMaxStackSize) {
        return (TemplateFishingRod) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateFishingRod setTexturePosition(int x, int y) {
        return (TemplateFishingRod) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateFishingRod setHasSubItems(boolean hasSubItems) {
        return (TemplateFishingRod) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateFishingRod setDurability(int durability) {
        return (TemplateFishingRod) super.setDurability(durability);
    }

    @Override
    public TemplateFishingRod setRendered3d() {
        return (TemplateFishingRod) super.setRendered3d();
    }

    @Override
    public TemplateFishingRod setTranslationKey(String newName) {
        return (TemplateFishingRod) super.setTranslationKey(newName);
    }

    @Override
    public TemplateFishingRod setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateFishingRod) super.setContainerItem(itemType);
    }

    @Override
    public Atlas getAtlas() {
        return ItemTemplate.super.getAtlas();
    }
}
