package net.modificationstation.stationapi.mixin.metablock;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.IsItemEffectiveOnBlockEvent;
import net.modificationstation.stationapi.api.event.item.ItemStrengthOnBlockEvent;
import net.modificationstation.stationapi.api.item.IsItemEffectiveOnMeta;
import net.modificationstation.stationapi.api.item.IsItemInstanceEffectiveOnMeta;
import net.modificationstation.stationapi.api.item.ItemInstanceStrengthOnMetaBlock;
import net.modificationstation.stationapi.api.item.ItemStrengthOnMetaBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInstance.class)
public abstract class MixinItemInstance implements IsItemInstanceEffectiveOnMeta, ItemInstanceStrengthOnMetaBlock {

    @Shadow public abstract boolean isEffectiveOn(BlockBase arg);

    @Shadow public abstract float getStrengthOnBlock(BlockBase arg);

    @Override
    public boolean isEffectiveOn(BlockBase tile, int meta) {
        this.meta = meta;
        boolean ret = isEffectiveOn(tile);
        this.meta = null;
        return ret;
    }

    @Redirect(method = "isEffectiveOn(Lnet/minecraft/block/BlockBase;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemBase;isEffectiveOn(Lnet/minecraft/block/BlockBase;)Z"))
    private boolean isEffectiveOn(ItemBase itemBase, BlockBase tile) {
        return meta == null ? itemBase.isEffectiveOn(tile) : StationAPI.EVENT_BUS.post(new IsItemEffectiveOnBlockEvent((ItemInstance) (Object) this, tile, meta, ((IsItemEffectiveOnMeta) itemBase).isEffectiveOn((ItemInstance) (Object) this, tile, meta))).effective;
    }

    @Override
    public float getStrengthOnBlock(BlockBase block, int meta) {
        this.meta = meta;
        float ret = getStrengthOnBlock(block);
        this.meta = null;
        return ret;
    }

    @Redirect(method = "getStrengthOnBlock(Lnet/minecraft/block/BlockBase;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemBase;getStrengthOnBlock(Lnet/minecraft/item/ItemInstance;Lnet/minecraft/block/BlockBase;)F"))
    private float getStrengthOnBlock(ItemBase itemBase, ItemInstance item, BlockBase tile) {
        return meta == null ? itemBase.getStrengthOnBlock(item, tile) : StationAPI.EVENT_BUS.post(new ItemStrengthOnBlockEvent(item, tile, meta, ((ItemStrengthOnMetaBlock) itemBase).getStrengthOnBlock(item, tile, meta))).strength;
    }

    private Integer meta;
}
