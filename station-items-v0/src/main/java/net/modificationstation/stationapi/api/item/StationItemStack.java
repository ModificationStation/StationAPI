package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.mixin.item.MixinPlayerBase;

public interface StationItemStack extends StationNBT {

    @Override
    default CompoundTag getStationNBT() {
        return Util.assertImpl();
    }

    default boolean preHit(EntityBase otherEntity, PlayerBase player){ return Util.assertImpl(); }

    default boolean preMine(BlockState blockState, int x, int y, int z, int side, PlayerBase player){ return Util.assertImpl(); }
}
