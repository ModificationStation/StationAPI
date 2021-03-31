package net.modificationstation.sltest.item.entity;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.item.ItemEntity;

import java.util.*;

public class NBTItemEntity implements ItemEntity {

    public NBTItemEntity() {
        this(rand.nextInt());
    }

    public NBTItemEntity(CompoundTag tag) {
        this(tag.getInt("sltest.displayNumber"));
    }

    private NBTItemEntity(int displayNumber) {
        this.displayNumber = displayNumber;
    }

    @Override
    public ItemEntity copy() {
        return new NBTItemEntity(displayNumber);
    }

    @Override
    public void writeToNBT(CompoundTag compoundTag) {
        compoundTag.put("sltest.displayNumber", getDisplayNumber());
    }

    public int getDisplayNumber() {
        return displayNumber;
    }

    private final int displayNumber;

    private static final Random rand = new Random();
}
