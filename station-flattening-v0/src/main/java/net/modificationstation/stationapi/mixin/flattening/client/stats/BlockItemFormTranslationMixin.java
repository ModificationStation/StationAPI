package net.modificationstation.stationapi.mixin.flattening.client.stats;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.stat.ItemOrBlockStat;
import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = {
        "net.minecraft.client.gui.screen.StatsScreen$AbstractStatsListWidget",
        "net.minecraft.client.gui.screen.StatsScreen$BlockStatsListWidget"
})
class BlockItemFormTranslationMixin {
    @Unique
    private final Reference2IntMap<ItemOrBlockStat> stationapi_idCache = Util.make(
            new Reference2IntOpenHashMap<>(),
            map -> map.defaultReturnValue(-1)
    );

    @WrapOperation(
            method = "*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/ItemOrBlockStat;getItemOrBlockId()I"
            ),
            require = 0
    )
    private int stationapi_translateToBlockItemForm(ItemOrBlockStat instance, Operation<Integer> original) {
        var cachedId = stationapi_idCache.getInt(instance);
        if (cachedId > -1)
            return cachedId;

        int id = original.call(instance);
        if (Stats.BLOCK_MINED_STATS.contains(instance)) {
            var item = Block.BLOCKS[id].asItem();
            if (item != null)
                id = item.id;
        }

        stationapi_idCache.put(instance, id);
        return id;
    }
}
