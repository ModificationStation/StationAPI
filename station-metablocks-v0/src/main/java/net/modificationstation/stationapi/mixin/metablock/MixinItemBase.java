package net.modificationstation.stationapi.mixin.metablock;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.item.EffectiveOnMeta;
import net.modificationstation.stationapi.api.item.StrengthOnMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBase.class)
public class MixinItemBase implements EffectiveOnMeta, StrengthOnMeta {

    @Shadow
    public boolean isEffectiveOn(BlockBase tile) {
        return false;
    }

    @Shadow
    public float getStrengthOnBlock(ItemInstance item, BlockBase tile) {
        return 0;
    }

    @Override
    public boolean isEffectiveOn(BlockBase tile, int meta, ItemInstance itemInstance) {
        boolean effective = isEffectiveOn(tile);
//        if (this instanceof ToolLevel) {
//            effective =
//                    ((BlockMiningLevel) tile).getToolTypes(meta, itemInstance) != null &&
//                            ((BlockMiningLevel) tile).getToolTypes(meta, itemInstance).stream().anyMatch(entry -> entry != null && entry.isInstance(itemInstance.getType())) &&
//                            ((ToolLevel) this).getToolLevel() >= ((BlockMiningLevel) tile).getBlockLevel(meta, itemInstance);
//            if (this instanceof OverrideIsEffectiveOn) {
//                effective = ((OverrideIsEffectiveOn) this).overrideIsEffectiveOn((ToolLevel) this, tile, meta, effective);
//            }
//        }
        return effective;
    }

    @Override
    public float getStrengthOnBlock(ItemInstance item, BlockBase tile, int meta) {
//        if (
//                item.getType() instanceof ToolLevel &&
//                        ((BlockMiningLevel) tile).getBlockLevel(meta, item) <= ((ToolLevel) item.getType()).getToolLevel() &&
//                        ((BlockMiningLevel) tile).getBlockLevel(meta, item) != -1 &&
//                        ((BlockMiningLevel) tile).getToolTypes(meta, item) != null &&
//                        ((BlockMiningLevel) tile).getToolTypes(meta, item).stream().anyMatch((toolLevel) -> toolLevel != null && toolLevel.isInstance(item.getType()))
//        ) {
//            return ((ToolLevel) item.getType()).getMaterial().getMiningSpeed();
//        }
//        else {
            return getStrengthOnBlock(item, tile);
//        }
    }
}
