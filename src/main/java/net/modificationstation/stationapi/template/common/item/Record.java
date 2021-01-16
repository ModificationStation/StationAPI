package net.modificationstation.stationapi.template.common.item;

import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Record extends net.minecraft.item.Record {

    public Record(Identifier identifier, String title) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), title);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Record(int id, String title) {
        super(id, title);
    }

    @Override
    public Record setTexturePosition(int texturePosition) {
        return (Record) super.setTexturePosition(texturePosition);
    }

    @Override
    public Record setMaxStackSize(int newMaxStackSize) {
        return (Record) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public Record setTexturePosition(int x, int y) {
        return (Record) super.setTexturePosition(x, y);
    }

    @Override
    public Record setHasSubItems(boolean hasSubItems) {
        return (Record) super.setHasSubItems(hasSubItems);
    }

    @Override
    public Record setDurability(int durability) {
        return (Record) super.setDurability(durability);
    }

    @Override
    public Record setRendered3d() {
        return (Record) super.setRendered3d();
    }

    @Override
    public Record setName(String newName) {
        return (Record) super.setName(newName);
    }

    @Override
    public Record setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (Record) super.setContainerItem(itemType);
    }
}
