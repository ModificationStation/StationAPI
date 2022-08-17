package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateSecondaryBlock extends net.minecraft.item.SecondaryBlock implements ItemTemplate<TemplateSecondaryBlock> {

    public TemplateSecondaryBlock(Identifier identifier, BlockBase tile) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted(), tile);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateSecondaryBlock(int id, BlockBase tile) {
        super(id, tile);
    }

    @Override
    public TemplateSecondaryBlock setTexturePosition(int texturePosition) {
        return (TemplateSecondaryBlock) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateSecondaryBlock setMaxStackSize(int newMaxStackSize) {
        return (TemplateSecondaryBlock) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateSecondaryBlock setTexturePosition(int x, int y) {
        return (TemplateSecondaryBlock) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateSecondaryBlock setHasSubItems(boolean hasSubItems) {
        return (TemplateSecondaryBlock) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateSecondaryBlock setDurability(int durability) {
        return (TemplateSecondaryBlock) super.setDurability(durability);
    }

    @Override
    public TemplateSecondaryBlock setRendered3d() {
        return (TemplateSecondaryBlock) super.setRendered3d();
    }

    @Override
    public TemplateSecondaryBlock setTranslationKey(String newName) {
        return (TemplateSecondaryBlock) super.setTranslationKey(newName);
    }

    @Override
    public TemplateSecondaryBlock setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateSecondaryBlock) super.setContainerItem(itemType);
    }
}
