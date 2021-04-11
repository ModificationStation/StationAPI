package net.modificationstation.stationapi.mixin.metablock;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.entity.player.StrengthOnMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerBase.class)
public abstract class MixinPlayerBase implements StrengthOnMeta {

    private Integer meta;

    @Shadow
    public abstract float getStrengh(BlockBase arg);

    @Override
    public float getStrengh(BlockBase arg, int meta) {
        this.meta = meta;
        return getStrengh(arg);
    }

    @Redirect(method = "getStrengh(Lnet/minecraft/block/BlockBase;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;getStrengthOnBlock(Lnet/minecraft/block/BlockBase;)F"))
    private float getStrengthForMeta(PlayerInventory playerInventory, BlockBase arg) {
        if (meta == null)
            return playerInventory.getStrengthOnBlock(arg);
        else {
            ItemInstance itemInstance = playerInventory.getHeldItem();
            float ret = itemInstance == null ? playerInventory.getStrengthOnBlock(arg) : ((net.modificationstation.stationapi.api.item.StrengthOnMeta) itemInstance.getType()).getStrengthOnBlock(itemInstance, arg, meta);
            meta = null;
            return ret;
        }
    }
}
