package net.modificationstation.stationapi.mixin.item;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.item.ItemStackEvent;
import net.modificationstation.stationapi.api.item.StationItemStack;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Mixin(ItemStack.class)
abstract class ItemStackMixin implements StationItemStack, StationNBTSetter {
    @Shadow
    public int itemId;

    @Shadow public abstract Item getItem();

    @Inject(
            method = "onCraft",
            at = @At("RETURN")
    )
    private void stationapi_onCreation(World arg, PlayerEntity arg1, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                ItemStackEvent.Crafted.builder()
                        .itemStack(ItemStack.class.cast(this))
                        .world(arg)
                        .player(arg1)
                        .build()
        );
    }

    @Unique
    @Getter @Setter
    private NbtCompound stationapi_stationNbt = new NbtCompound();

    @Inject(
            method = "split",
            at = @At("RETURN")
    )
    private void stationapi_setSplitStackNbt(int par1, CallbackInfoReturnable<ItemStack> cir) {
        if (!stationapi_stationNbt.values().isEmpty())
            StationNBTSetter.cast(cir.getReturnValue()).setStationNbt(stationapi_stationNbt.copy());
    }

    @Inject(
            method = "writeNbt",
            at = @At("RETURN")
    )
    private void stationapi_toTagCustom(NbtCompound par1, CallbackInfoReturnable<NbtCompound> cir) {
        if (!stationapi_stationNbt.values().isEmpty())
            cir.getReturnValue().put(of(NAMESPACE, "item_nbt").toString(), stationapi_stationNbt);
    }

    @Inject(
            method = "readNbt",
            at = @At("RETURN")
    )
    private void stationapi_fromTagCustom(NbtCompound par1, CallbackInfo ci) {
        stationapi_stationNbt = par1.getCompound(of(NAMESPACE, "item_nbt").toString());
    }

    @Inject(
            method = "copy",
            at = @At("RETURN")
    )
    private void stationapi_copy(CallbackInfoReturnable<ItemStack> cir) {
        if (!stationapi_stationNbt.values().isEmpty())
            StationNBTSetter.cast(cir.getReturnValue()).setStationNbt(stationapi_stationNbt.copy());
    }

    @Inject(
            method = "equals2",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_isStackIdentical(ItemStack par1, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(stationapi_stationNbt, par1.getStationNbt()))
            cir.setReturnValue(false);
    }

    @Inject(
            method = "isItemEqual",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_isDamageAndIDIdentical(ItemStack par1, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(stationapi_stationNbt, par1.getStationNbt()))
            cir.setReturnValue(false);
    }

    @Inject(
            method = "toString",
            at = @At("RETURN"),
            cancellable = true
    )
    private void stationapi_toStringNBT(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(cir.getReturnValue() + ", stationNBT=[" + stationapi_stationNbt + "]");
    }

    @Inject(
            method = "equals",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_isStackIdentical2(ItemStack par1, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(stationapi_stationNbt, par1.getStationNbt()))
            cir.setReturnValue(false);
    }

    @Override
    @Unique
    public boolean preHit(Entity otherEntity, PlayerEntity player) {
        return getItem().preHit(ItemStack.class.cast(this), otherEntity, player);
    }

    @Override
    @Unique
    public boolean preMine(BlockState blockState, int x, int y, int z, int side, PlayerEntity player) {
        return getItem().preMine(ItemStack.class.cast(this), blockState, x, y, z, side, player);
    }

    @Redirect(
            method = "getMaxDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;getMaxDamage()I"
            )
    )
    private int stationapi_getDurabilityPerStack(Item instance) {
        return instance.getMaxDamage(ItemStack.class.cast(this));
    }

    @Redirect(
            method = "isDamageable",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;getMaxDamage()I"
            )
    )
    private int stationapi_hasDurability_getDurabilityPerStack(Item instance) {
        return instance.getMaxDamage(ItemStack.class.cast(this));
    }
}
