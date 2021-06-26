package net.modificationstation.stationapi.api.template.item;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateRecord extends net.minecraft.item.Record implements ItemTemplate<TemplateRecord> {

    public TemplateRecord(Identifier identifier, String title) {
        this(ItemRegistry.INSTANCE.getNextSerialIDShifted(), title);
        ItemRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateRecord(int id, String title) {
        super(id, title);
    }

    @Override
    public TemplateRecord setTexturePosition(int texturePosition) {
        return (TemplateRecord) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateRecord setMaxStackSize(int newMaxStackSize) {
        return (TemplateRecord) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateRecord setTexturePosition(int x, int y) {
        return (TemplateRecord) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateRecord setHasSubItems(boolean hasSubItems) {
        return (TemplateRecord) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateRecord setDurability(int durability) {
        return (TemplateRecord) super.setDurability(durability);
    }

    @Override
    public TemplateRecord setRendered3d() {
        return (TemplateRecord) super.setRendered3d();
    }

    @Override
    public TemplateRecord setTranslationKey(String newName) {
        return (TemplateRecord) super.setTranslationKey(newName);
    }

    @Override
    public TemplateRecord setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateRecord) super.setContainerItem(itemType);
    }
}
