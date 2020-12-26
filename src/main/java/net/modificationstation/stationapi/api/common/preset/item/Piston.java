package net.modificationstation.stationapi.api.common.preset.item;

public class Piston extends net.minecraft.item.Piston {
    
    public Piston(int i) {
        super(i);
    }

    @Override
    public Piston setTexturePosition(int texturePosition) {
        return (Piston) super.setTexturePosition(texturePosition);
    }

    @Override
    public Piston setMaxStackSize(int newMaxStackSize) {
        return (Piston) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Piston setTexturePosition(int x, int y) {
        return (Piston) super.setTexturePosition(x, y);
    }

    @Override
    public Piston setHasSubItems(boolean hasSubItems) {
        return (Piston) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Piston setDurability(int durability) {
        return (Piston) super.setDurability(durability);
    }

    @Override
    public Piston setRendered3d() {
        return (Piston) super.setRendered3d();
    }

    @Override
    public Piston setName(String newName) {
        return (Piston) super.setName(newName);
    }

    @Override
    public Piston setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Piston) super.setContainerItem(itemType);
    }
}
