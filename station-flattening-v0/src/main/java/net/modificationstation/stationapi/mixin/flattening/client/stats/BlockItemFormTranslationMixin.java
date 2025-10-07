package net.modificationstation.stationapi.mixin.flattening.client.stats;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.stat.ItemOrBlockStat;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = {
        "net.minecraft.client.gui.screen.StatsScreen$AbstractStatsListWidget",
        "net.minecraft.client.gui.screen.StatsScreen$BlockStatsListWidget"
})
class BlockItemFormTranslationMixin {
    @WrapOperation(
            method = "*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/ItemOrBlockStat;getItemOrBlockId()I"
            ),
            require = 0
    )
    private int stationapi_translateToBlockItemForm(ItemOrBlockStat instance, Operation<Integer> original) {
        int id = original.call(instance);
        if (!Stats.BLOCK_MINED_STATS.contains(instance))
            return id;

        var item = Block.BLOCKS[id].asItem();
        if (item == null)
            return id;

        return item.id;
    }

}
