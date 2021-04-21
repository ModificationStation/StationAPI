package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateDoor extends net.minecraft.item.Door implements IItemTemplate<TemplateDoor> {
    
    public TemplateDoor(Identifier identifier, Material arg) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), arg);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateDoor(int id, Material arg) {
        super(id, arg);
    }

    @Override
    public TemplateDoor setTexturePosition(int texturePosition) {
        return (TemplateDoor) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateDoor setMaxStackSize(int newMaxStackSize) {
        return (TemplateDoor) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateDoor setTexturePosition(int x, int y) {
        return (TemplateDoor) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateDoor setHasSubItems(boolean hasSubItems) {
        return (TemplateDoor) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateDoor setDurability(int durability) {
        return (TemplateDoor) super.setDurability(durability);
    }

    @Override
    public TemplateDoor setRendered3d() {
        return (TemplateDoor) super.setRendered3d();
    }

    @Override
    public TemplateDoor setTranslationKey(String newName) {
        return (TemplateDoor) super.setTranslationKey(newName);
    }

    @Override
    public TemplateDoor setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateDoor) super.setContainerItem(itemType);
    }
}
