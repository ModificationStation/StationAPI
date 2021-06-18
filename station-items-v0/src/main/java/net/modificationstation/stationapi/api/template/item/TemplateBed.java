package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateBed extends net.minecraft.item.Bed implements ItemTemplate<TemplateBed> {

    public TemplateBed(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerialID());
        ItemRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateBed(int i) {
        super(i);
    }

    @Override
    public TemplateBed setTexturePosition(int texturePosition) {
        return (TemplateBed) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateBed setMaxStackSize(int newMaxStackSize) {
        return (TemplateBed) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateBed setTexturePosition(int x, int y) {
        return (TemplateBed) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateBed setHasSubItems(boolean hasSubItems) {
        return (TemplateBed) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateBed setDurability(int durability) {
        return (TemplateBed) super.setDurability(durability);
    }

    @Override
    public TemplateBed setRendered3d() {
        return (TemplateBed) super.setRendered3d();
    }

    @Override
    public TemplateBed setTranslationKey(String newName) {
        return (TemplateBed) super.setTranslationKey(newName);
    }

    @Override
    public TemplateBed setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateBed) super.setContainerItem(itemType);
    }
}
