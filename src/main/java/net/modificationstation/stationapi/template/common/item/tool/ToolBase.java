package net.modificationstation.stationapi.template.common.item.tool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class ToolBase extends net.minecraft.item.tool.ToolBase {
    
    public ToolBase(Identifier identifier, int j, ToolMaterial arg, BlockBase[] effectiveBlocks) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), j, arg, effectiveBlocks);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public ToolBase(int id, int j, ToolMaterial arg, BlockBase[] effectiveBlocks) {
        super(id, j, arg, effectiveBlocks);
    }

    @Override
    public ToolBase setTexturePosition(int texturePosition) {
        return (ToolBase) super.setTexturePosition(texturePosition);
    }

    @Override
    public ToolBase setMaxStackSize(int newMaxStackSize) {
        return (ToolBase) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public ToolBase setTexturePosition(int x, int y) {
        return (ToolBase) super.setTexturePosition(x, y);
    }

    @Override
    public ToolBase setHasSubItems(boolean hasSubItems) {
        return (ToolBase) super.setHasSubItems(hasSubItems);
    }

    @Override
    public ToolBase setDurability(int durability) {
        return (ToolBase) super.setDurability(durability);
    }

    @Override
    public ToolBase setRendered3d() {
        return (ToolBase) super.setRendered3d();
    }

    @Override
    public ToolBase setName(String newName) {
        return (ToolBase) super.setTranslationKey(newName);
    }

    @Override
    public ToolBase setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (ToolBase) super.setContainerItem(itemType);
    }
}
