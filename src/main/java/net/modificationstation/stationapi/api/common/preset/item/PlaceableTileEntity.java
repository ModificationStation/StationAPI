package net.modificationstation.stationapi.api.common.preset.item;

public class PlaceableTileEntity extends net.minecraft.item.PlaceableTileEntity {
    
    public PlaceableTileEntity(int i) {
        super(i);
    }

    @Override
    public PlaceableTileEntity setTexturePosition(int texturePosition) {
        return (PlaceableTileEntity) super.setTexturePosition(texturePosition);
    }

    @Override
    public PlaceableTileEntity setMaxStackSize(int newMaxStackSize) {
        return (PlaceableTileEntity) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public PlaceableTileEntity setTexturePosition(int x, int y) {
        return (PlaceableTileEntity) super.setTexturePosition(x, y);
    }

    @Override
    public PlaceableTileEntity setHasSubItems(boolean hasSubItems) {
        return (PlaceableTileEntity) super.setHasSubItems(hasSubItems);
    }

    @Override
    public PlaceableTileEntity setDurability(int durability) {
        return (PlaceableTileEntity) super.setDurability(durability);
    }

    @Override
    public PlaceableTileEntity setRendered3d() {
        return (PlaceableTileEntity) super.setRendered3d();
    }

    @Override
    public PlaceableTileEntity setName(String newName) {
        return (PlaceableTileEntity) super.setName(newName);
    }

    @Override
    public PlaceableTileEntity setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (PlaceableTileEntity) super.setContainerItem(itemType);
    }
}
