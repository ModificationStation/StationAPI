package net.modificationstation.stationapi.mixin.metablock;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.entity.player.InventoryStrengthOnMetaBlock;
import net.modificationstation.stationapi.api.entity.player.IsInventoryUsingEffectiveToolOnMeta;
import net.modificationstation.stationapi.api.item.IsItemInstanceEffectiveOnMeta;
import net.modificationstation.stationapi.api.item.ItemInstanceStrengthOnMetaBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerInventory.class)
public abstract class MixinPlayerInventory implements IsInventoryUsingEffectiveToolOnMeta, InventoryStrengthOnMetaBlock {

    @Shadow public abstract boolean isUsingEffectiveTool(BlockBase arg);

    @Shadow public abstract float getStrengthOnBlock(BlockBase arg);

    @Override
    public boolean isUsingEffectiveTool(BlockBase block, int meta) {
        this.meta = meta;
        boolean ret = isUsingEffectiveTool(block);
        this.meta = null;
        return ret;
    }

    @Redirect(method = "isUsingEffectiveTool(Lnet/minecraft/block/BlockBase;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemInstance;isEffectiveOn(Lnet/minecraft/block/BlockBase;)Z"))
    private boolean isEffectiveOn(ItemInstance itemInstance, BlockBase arg) {
        return meta == null ? itemInstance.isEffectiveOn(arg) : IsItemInstanceEffectiveOnMeta.cast(itemInstance).isEffectiveOn(arg, meta);
    }

    @Override
    public float getStrengthOnBlock(BlockBase arg, int meta) {
        this.meta = meta;
        float ret = getStrengthOnBlock(arg);
        this.meta = null;
        return ret;
    }

    @Redirect(method = "getStrengthOnBlock(Lnet/minecraft/block/BlockBase;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemInstance;getStrengthOnBlock(Lnet/minecraft/block/BlockBase;)F"))
    private float getStrengthOnBlock(ItemInstance itemInstance, BlockBase arg) {
        return meta == null ? itemInstance.getStrengthOnBlock(arg) : ItemInstanceStrengthOnMetaBlock.cast(itemInstance).getStrengthOnBlock(arg, meta);
    }

    private Integer meta;
}
