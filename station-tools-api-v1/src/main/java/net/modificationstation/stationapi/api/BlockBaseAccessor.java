package net.modificationstation.stationapi.api;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.List;

public interface BlockBaseAccessor {

    BlockBase mineableBy(Identifier toolTag, int level);
    List<BiTuple<Identifier, Integer>> getToolTagEffectiveness();

}
