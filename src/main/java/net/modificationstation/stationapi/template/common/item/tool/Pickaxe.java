package net.modificationstation.stationapi.template.common.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Pickaxe extends net.minecraft.item.tool.Pickaxe {
    
    public Pickaxe(Identifier identifier, ToolMaterial material) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), material);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Pickaxe(int id, ToolMaterial material) {
        super(id, material);
    }

    @Override
    public Pickaxe setTexturePosition(int texturePosition) {
        return (Pickaxe) super.setTexturePosition(texturePosition);
    }

    @Override
    public Pickaxe setMaxStackSize(int newMaxStackSize) {
        return (Pickaxe) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Pickaxe setTexturePosition(int x, int y) {
        return (Pickaxe) super.setTexturePosition(x, y);
    }

    @Override
    public Pickaxe setHasSubItems(boolean hasSubItems) {
        return (Pickaxe) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Pickaxe setDurability(int durability) {
        return (Pickaxe) super.setDurability(durability);
    }

    @Override
    public Pickaxe setRendered3d() {
        return (Pickaxe) super.setRendered3d();
    }

    @Override
    public Pickaxe setName(String newName) {
        return (Pickaxe) super.setName(newName);
    }

    @Override
    public Pickaxe setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Pickaxe) super.setContainerItem(itemType);
    }
}
