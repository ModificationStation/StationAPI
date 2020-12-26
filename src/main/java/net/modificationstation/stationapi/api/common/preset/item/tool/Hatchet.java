package net.modificationstation.stationapi.api.common.preset.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Hatchet extends net.minecraft.item.tool.Hatchet {
    
    public Hatchet(Identifier identifier, ToolMaterial material) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), material);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Hatchet(int id, ToolMaterial material) {
        super(id, material);
    }

    @Override
    public Hatchet setTexturePosition(int texturePosition) {
        return (Hatchet) super.setTexturePosition(texturePosition);
    }

    @Override
    public Hatchet setMaxStackSize(int newMaxStackSize) {
        return (Hatchet) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Hatchet setTexturePosition(int x, int y) {
        return (Hatchet) super.setTexturePosition(x, y);
    }

    @Override
    public Hatchet setHasSubItems(boolean hasSubItems) {
        return (Hatchet) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Hatchet setDurability(int durability) {
        return (Hatchet) super.setDurability(durability);
    }

    @Override
    public Hatchet setRendered3d() {
        return (Hatchet) super.setRendered3d();
    }

    @Override
    public Hatchet setName(String newName) {
        return (Hatchet) super.setName(newName);
    }

    @Override
    public Hatchet setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Hatchet) super.setContainerItem(itemType);
    }
}
