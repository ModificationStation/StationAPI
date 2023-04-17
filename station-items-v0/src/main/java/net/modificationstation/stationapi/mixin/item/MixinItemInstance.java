package net.modificationstation.stationapi.mixin.item;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.ItemStackEvent;
import net.modificationstation.stationapi.api.item.StationItemStack;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.impl.item.nbt.StationNBTSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Mixin(ItemInstance.class)
public abstract class MixinItemInstance implements StationItemStack, StationNBTSetter {

    @Shadow
    public int itemId;

    @Shadow public abstract ItemBase getType();

    @Inject(method = "onCrafted(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;)V", at = @At("RETURN"))
    private void onCreation(Level arg, PlayerBase arg1, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                ItemStackEvent.Crafted.builder()
                        .itemStack(ItemInstance.class.cast(this))
                        .level(arg)
                        .player(arg1)
                        .build()
        );
    }

    @Unique
    @Getter @Setter
    private CompoundTag stationNBT = new CompoundTag();

    @Inject(
            method = "split(I)Lnet/minecraft/item/ItemInstance;",
            at = @At("RETURN")
    )
    private void setSplitStackNbt(int par1, CallbackInfoReturnable<ItemInstance> cir) {
        if (!stationNBT.values().isEmpty())
            StationNBTSetter.cast(cir.getReturnValue()).setStationNBT(stationNBT.copy());
    }

    @Inject(
            method = "toTag(Lnet/minecraft/util/io/CompoundTag;)Lnet/minecraft/util/io/CompoundTag;",
            at = @At("RETURN")
    )
    private void toTagCustom(CompoundTag par1, CallbackInfoReturnable<CompoundTag> cir) {
        if (!stationNBT.values().isEmpty())
            cir.getReturnValue().put(of(MODID, "item_nbt").toString(), stationNBT);
    }

    @Inject(
            method = "fromTag(Lnet/minecraft/util/io/CompoundTag;)V",
            at = @At("RETURN")
    )
    private void fromTagCustom(CompoundTag par1, CallbackInfo ci) {
        stationNBT = par1.getCompoundTag(of(MODID, "item_nbt").toString());
    }

    @Inject(
            method = "copy()Lnet/minecraft/item/ItemInstance;",
            at = @At("RETURN")
    )
    private void copy(CallbackInfoReturnable<ItemInstance> cir) {
        if (!stationNBT.values().isEmpty())
            StationNBTSetter.cast(cir.getReturnValue()).setStationNBT(stationNBT.copy());
    }

    @Inject(
            method = "isStackIdentical(Lnet/minecraft/item/ItemInstance;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isStackIdentical(ItemInstance par1, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(stationNBT, par1.getStationNBT()))
            cir.setReturnValue(false);
    }

    @Inject(
            method = "isDamageAndIDIdentical(Lnet/minecraft/item/ItemInstance;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isDamageAndIDIdentical(ItemInstance par1, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(stationNBT, par1.getStationNBT()))
            cir.setReturnValue(false);
    }

    @Inject(
            method = "toString()Ljava/lang/String;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void toStringNBT(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(cir.getReturnValue() + ", stationNBT=[" + stationNBT + "]");
    }

    @Inject(
            method = "isStackIdentical2(Lnet/minecraft/item/ItemInstance;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isStackIdentical2(ItemInstance par1, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(stationNBT, par1.getStationNBT()))
            cir.setReturnValue(false);
    }

    @Override
    public RegistryEntry.Reference<ItemBase> getRegistryEntry() {
        return getType().getRegistryEntry();
    }
}
