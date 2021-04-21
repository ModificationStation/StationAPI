package net.modificationstation.stationapi.mixin.metablock;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.entity.player.CanPlayerRemoveMetaBlock;
import net.modificationstation.stationapi.api.entity.player.InventoryStrengthOnMetaBlock;
import net.modificationstation.stationapi.api.entity.player.IsInventoryUsingEffectiveToolOnMeta;
import net.modificationstation.stationapi.api.entity.player.PlayerStrengthOnMetaBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerBase.class)
public abstract class MixinPlayerBase implements PlayerStrengthOnMetaBlock, CanPlayerRemoveMetaBlock {

    private Integer meta;

    @Shadow
    public abstract float getStrengh(BlockBase arg);

    @Shadow public abstract boolean canRemoveBlock(BlockBase arg);

    @Override
    public float getStrengh(BlockBase arg, int meta) {
        this.meta = meta;
        float ret = getStrengh(arg);
        this.meta = null;
        return ret;
    }

    @Redirect(method = "getStrengh(Lnet/minecraft/block/BlockBase;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getStrengthOnBlock(Lnet/minecraft/block/BlockBase;)F"))
    private float getStrength(PlayerInventory playerInventory, BlockBase arg) {
        return meta == null ? playerInventory.getStrengthOnBlock(arg) : ((InventoryStrengthOnMetaBlock) playerInventory).getStrengthOnBlock(arg, meta);
//        if (meta == null)
//            return playerInventory.getStrengthOnBlock(arg);
//        else {
//            ItemInstance itemInstance = playerInventory.getHeldItem();
//            float ret = itemInstance == null ? playerInventory.getStrengthOnBlock(arg) : ((net.modificationstation.stationapi.api.item.StrengthOnMeta) itemInstance.getType()).getStrengthOnBlock(itemInstance, arg, meta);
//            meta = null;
//            return ret;
//        }
    }

    @Override
    public boolean canRemoveBlock(BlockBase block, int meta) {
        this.meta = meta;
        boolean ret = canRemoveBlock(block);
        this.meta = null;
        return ret;
//        return ((IsInventoryUsingEffectiveToolOnMeta) inventory).isUsingEffectiveTool(block, meta);
    }

    @Redirect(method = "canRemoveBlock(Lnet/minecraft/block/BlockBase;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;isUsingEffectiveTool(Lnet/minecraft/block/BlockBase;)Z"))
    private boolean canRemoveMetaBlock(PlayerInventory playerInventory, BlockBase arg) {
        return meta == null ? playerInventory.isUsingEffectiveTool(arg) : ((IsInventoryUsingEffectiveToolOnMeta) playerInventory).isUsingEffectiveTool(arg, meta);
    }
}
