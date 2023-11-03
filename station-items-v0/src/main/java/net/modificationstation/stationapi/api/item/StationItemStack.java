package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.util.Util;

public interface StationItemStack extends StationNBT {

    @Override
    default NbtCompound getStationNBT() {
        return Util.assertImpl();
    }

    default boolean preHit(Entity otherEntity, PlayerEntity player){ return Util.assertImpl(); }

    default boolean preMine(BlockState blockState, int x, int y, int z, int side, PlayerEntity player){ return Util.assertImpl(); }
}
