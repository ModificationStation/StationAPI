package net.modificationstation.stationapi.template.common.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.template.common.item.IItemTemplate;

public class Hoe extends net.minecraft.item.tool.Hoe implements IItemTemplate<Hoe> {

    public Hoe(Identifier identifier, ToolMaterial arg) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), arg);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Hoe(int i, ToolMaterial arg) {
        super(i, arg);
    }

    @Override
    public Hoe setTexturePosition(int texturePosition) {
        return (Hoe) super.setTexturePosition(texturePosition);
    }

    @Override
    public Hoe setMaxStackSize(int newMaxStackSize) {
        return (Hoe) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Hoe setTexturePosition(int x, int y) {
        return (Hoe) super.setTexturePosition(x, y);
    }

    @Override
    public Hoe setHasSubItems(boolean hasSubItems) {
        return (Hoe) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Hoe setDurability(int durability) {
        return (Hoe) super.setDurability(durability);
    }

    @Override
    public Hoe setRendered3d() {
        return (Hoe) super.setRendered3d();
    }

    @Override
    public Hoe setTranslationKey(String newName) {
        return (Hoe) super.setTranslationKey(newName);
    }

    @Override
    public Hoe setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Hoe) super.setContainerItem(itemType);
    }
}
