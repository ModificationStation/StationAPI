package net.modificationstation.stationapi.template.common.item;

public class Block extends net.minecraft.item.PlaceableTileEntity {
    
    public Block(int i) {
        super(i);
    }

    @Override
    public Block setTexturePosition(int texturePosition) {
        return (Block) super.setTexturePosition(texturePosition);
    }

    @Override
    public Block setMaxStackSize(int newMaxStackSize) {
        return (Block) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Block setTexturePosition(int x, int y) {
        return (Block) super.setTexturePosition(x, y);
    }

    @Override
    public Block setHasSubItems(boolean hasSubItems) {
        return (Block) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Block setDurability(int durability) {
        return (Block) super.setDurability(durability);
    }

    @Override
    public Block setRendered3d() {
        return (Block) super.setRendered3d();
    }

    @Override
    public Block setName(String newName) {
        return (Block) super.setName(newName);
    }

    @Override
    public Block setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Block) super.setContainerItem(itemType);
    }
}
