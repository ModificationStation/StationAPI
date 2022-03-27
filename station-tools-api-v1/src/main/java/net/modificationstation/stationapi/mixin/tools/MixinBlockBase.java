package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.BlockToolLogic;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.ArrayList;
import java.util.List;

@Mixin(BlockBase.class)
public class MixinBlockBase implements BlockToolLogic {

    private final List<BiTuple<Identifier, Integer>> toolTagEffectiveness = new ArrayList<>();

    @Override
    public BlockBase mineableBy(Identifier toolTag, int level) {
        toolTagEffectiveness.add(BiTuple.of(toolTag, level));
        return (BlockBase) (Object) this;
    }

    @Override
    public List<BiTuple<Identifier, Integer>> getToolTagEffectiveness() {
        return toolTagEffectiveness;
    }

}
