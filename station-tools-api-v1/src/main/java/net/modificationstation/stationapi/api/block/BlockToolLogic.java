package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.List;

public interface BlockToolLogic {

    BlockBase mineableBy(Identifier toolTag, int level);
    List<BiTuple<Identifier, Integer>> getToolTagEffectiveness();

}
