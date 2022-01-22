package net.modificationstation.stationapi.api.template.item.armour;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateArmour extends net.minecraft.item.armour.Armour implements ItemTemplate<TemplateArmour> {

    public TemplateArmour(Identifier identifier, int j, int k, int slot) {
        this(ItemRegistry.INSTANCE.getNextSerialIDShifted(), j, k, slot);
        ItemRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateArmour(int id, int j, int k, int slot) {
        super(id, j, k, slot);
    }

    @Override
    public TemplateArmour setTexturePosition(int texturePosition) {
        return (TemplateArmour) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateArmour setMaxStackSize(int newMaxStackSize) {
        return (TemplateArmour) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateArmour setTexturePosition(int x, int y) {
        return (TemplateArmour) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateArmour setHasSubItems(boolean hasSubItems) {
        return (TemplateArmour) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateArmour setDurability(int durability) {
        return (TemplateArmour) super.setDurability(durability);
    }

    @Override
    public TemplateArmour setRendered3d() {
        return (TemplateArmour) super.setRendered3d();
    }

    @Override
    public TemplateArmour setTranslationKey(String newName) {
        return (TemplateArmour) super.setTranslationKey(newName);
    }

    @Override
    public TemplateArmour setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateArmour) super.setContainerItem(itemType);
    }
}
