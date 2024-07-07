package net.modificationstation.stationapi.mixin.flattening.client.stats;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.gui.screen.StatsScreen$BlockStatsListWidget")
class BlockStatsListWidgetMixin {
    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/ItemOrBlockStat;getItemOrBlockId()I"
            )
    )
    private int stationapi_transformToItemId(int original) {
        final Item item = Block.BLOCKS[original].asItem();
        return item == null ? 0 : item.id;
    }
}
