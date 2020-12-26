package net.modificationstation.stationapi.api.common.preset.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Sword extends net.minecraft.item.tool.Sword {
    
    public Sword(Identifier identifier, ToolMaterial arg) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), arg);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Sword(int i, ToolMaterial arg) {
        super(i, arg);
    }

    @Override
    public Sword setTexturePosition(int texturePosition) {
        return (Sword) super.setTexturePosition(texturePosition);
    }

    @Override
    public Sword setMaxStackSize(int newMaxStackSize) {
        return (Sword) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Sword setTexturePosition(int x, int y) {
        return (Sword) super.setTexturePosition(x, y);
    }

    @Override
    public Sword setHasSubItems(boolean hasSubItems) {
        return (Sword) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Sword setDurability(int durability) {
        return (Sword) super.setDurability(durability);
    }

    @Override
    public Sword setRendered3d() {
        return (Sword) super.setRendered3d();
    }

    @Override
    public Sword setName(String newName) {
        return (Sword) super.setName(newName);
    }

    @Override
    public Sword setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Sword) super.setContainerItem(itemType);
    }
}
