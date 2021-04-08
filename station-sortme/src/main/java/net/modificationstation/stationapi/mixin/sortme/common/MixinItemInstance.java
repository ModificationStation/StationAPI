package net.modificationstation.stationapi.mixin.sortme.common;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.common.event.item.ItemInstanceEvent;
import net.modificationstation.stationapi.api.common.item.HasItemEntity;
import net.modificationstation.stationapi.api.common.item.ItemEntity;
import net.modificationstation.stationapi.api.common.item.ItemWithEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInstance.class)
public class MixinItemInstance implements HasItemEntity {

    @Shadow
    public int itemId;
    private ItemEntity itemEntity;

    @Inject(method = "<init>(III)V", at = @At("RETURN"))
    private void onFreshInstance(int id, int count, int damage, CallbackInfo ci) {
        ItemBase itemBase = ItemBase.byId[id];
        if (itemBase instanceof ItemWithEntity)
            itemEntity = ((ItemWithEntity) itemBase).getItemEntityFactory().get();
    }

    @Inject(method = "fromTag(Lnet/minecraft/util/io/CompoundTag;)V", at = @At("RETURN"))
    private void fromTag(CompoundTag tag, CallbackInfo ci) {
        ItemBase itemBase = ItemBase.byId[itemId];
        if (itemBase instanceof ItemWithEntity)
            itemEntity = ((ItemWithEntity) itemBase).getItemEntityNBTFactory().apply(tag);
    }

    @Inject(method = "toTag(Lnet/minecraft/util/io/CompoundTag;)Lnet/minecraft/util/io/CompoundTag;", at = @At("RETURN"))
    private void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        ItemBase itemBase = ItemBase.byId[itemId];
        if (itemBase instanceof ItemWithEntity)
            itemEntity.writeToNBT(tag);
    }

    @Inject(method = "split(I)Lnet/minecraft/item/ItemInstance;", at = @At("RETURN"))
    private void onSplit(int countToTake, CallbackInfoReturnable<ItemInstance> cir) {
        if (itemEntity != null)
            HasItemEntity.cast(cir.getReturnValue()).setItemEntity(itemEntity.split(countToTake));
    }

    @Inject(method = "copy()Lnet/minecraft/item/ItemInstance;", at = @At("RETURN"))
    private void onCopy(CallbackInfoReturnable<ItemInstance> cir) {
        if (itemEntity != null)
            HasItemEntity.cast(cir.getReturnValue()).setItemEntity(itemEntity.copy());
    }

    @Override
    public ItemEntity getItemEntity() {
        return itemEntity;
    }

    @Override
    public void setItemEntity(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }

    @Inject(method = "onCrafted(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;)V", at = @At("RETURN"))
    private void onCreation(Level arg, PlayerBase arg1, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new ItemInstanceEvent.Crafted((ItemInstance) (Object) this, arg, arg1));
    }
}
