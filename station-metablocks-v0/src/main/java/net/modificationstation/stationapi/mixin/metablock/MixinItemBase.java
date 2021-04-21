package net.modificationstation.stationapi.mixin.metablock;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.item.IsItemEffectiveOnMeta;
import net.modificationstation.stationapi.api.item.ItemStrengthOnMetaBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBase.class)
public abstract class MixinItemBase implements IsItemEffectiveOnMeta, ItemStrengthOnMetaBlock {

    @Shadow public abstract boolean isEffectiveOn(BlockBase tile);

    @Shadow public abstract float getStrengthOnBlock(ItemInstance item, BlockBase tile);

    @Override
    public boolean isEffectiveOn(ItemInstance itemInstance, BlockBase tile, int meta) {
        return isEffectiveOn(tile);
    }

    @Override
    public float getStrengthOnBlock(ItemInstance item, BlockBase tile, int meta) {
        return getStrengthOnBlock(item, tile);
    }
}
