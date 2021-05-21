package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateFlintAndSteel extends net.minecraft.item.FlintAndSteel implements ItemTemplate<TemplateFlintAndSteel> {

    public TemplateFlintAndSteel(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerializedID());
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateFlintAndSteel(int i) {
        super(i);
    }

    @Override
    public TemplateFlintAndSteel setTexturePosition(int texturePosition) {
        return (TemplateFlintAndSteel) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateFlintAndSteel setMaxStackSize(int newMaxStackSize) {
        return (TemplateFlintAndSteel) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateFlintAndSteel setTexturePosition(int x, int y) {
        return (TemplateFlintAndSteel) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateFlintAndSteel setHasSubItems(boolean hasSubItems) {
        return (TemplateFlintAndSteel) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateFlintAndSteel setDurability(int durability) {
        return (TemplateFlintAndSteel) super.setDurability(durability);
    }

    @Override
    public TemplateFlintAndSteel setRendered3d() {
        return (TemplateFlintAndSteel) super.setRendered3d();
    }

    @Override
    public TemplateFlintAndSteel setTranslationKey(String newName) {
        return (TemplateFlintAndSteel) super.setTranslationKey(newName);
    }

    @Override
    public TemplateFlintAndSteel setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateFlintAndSteel) super.setContainerItem(itemType);
    }
}
