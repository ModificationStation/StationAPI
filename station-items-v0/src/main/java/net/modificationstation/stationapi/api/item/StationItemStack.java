package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.mixin.item.MixinPlayerBase;

public interface StationItemStack extends StationNBT {

    @Override
    default CompoundTag getStationNBT() {
        return Util.assertImpl();
    }

    default boolean preHit(Living otherEntity, PlayerBase player){ return Util.assertImpl(); }

    default boolean preMine(int x, int y, int z, int l, Living entity){ return Util.assertImpl(); }
}
