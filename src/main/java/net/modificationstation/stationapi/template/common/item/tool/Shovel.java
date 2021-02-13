package net.modificationstation.stationapi.template.common.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Shovel extends net.minecraft.item.tool.Shovel {
    
    public Shovel(Identifier identifier, ToolMaterial arg) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), arg);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Shovel(int id, ToolMaterial arg) {
        super(id, arg);
    }

    @Override
    public Shovel setTexturePosition(int texturePosition) {
        return (Shovel) super.setTexturePosition(texturePosition);
    }

    @Override
    public Shovel setMaxStackSize(int newMaxStackSize) {
        return (Shovel) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Shovel setTexturePosition(int x, int y) {
        return (Shovel) super.setTexturePosition(x, y);
    }

    @Override
    public Shovel setHasSubItems(boolean hasSubItems) {
        return (Shovel) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Shovel setDurability(int durability) {
        return (Shovel) super.setDurability(durability);
    }

    @Override
    public Shovel setRendered3d() {
        return (Shovel) super.setRendered3d();
    }

    @Override
    public Shovel setName(String newName) {
        return (Shovel) super.setTranslationKey(newName);
    }

    @Override
    public Shovel setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Shovel) super.setContainerItem(itemType);
    }
}
