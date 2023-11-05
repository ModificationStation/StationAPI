package net.modificationstation.stationapi.mixin.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.UseOnEntityFirst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity {
    private PlayerEntityMixin(World arg) {
        super(arg);
    }

    @Shadow public abstract ItemStack getHand();

    @Inject(
            method = "interact",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void stationapi_hijackInteractWith(Entity arg, CallbackInfo ci) {
        ItemStack stack = getHand();
        //noinspection DataFlowIssue
        if (stack != null && stack.getItem() instanceof UseOnEntityFirst use && use.onUseOnEntityFirst(stack, (PlayerEntity) (Object) this, this.world, arg))
            ci.cancel();
    }


    @Inject(
            method = "attack",
            at = @At("HEAD"),
            cancellable = true
    )
    public void stationapi_attack_preHit(Entity entity, CallbackInfo ci) {
        ItemStack stack = this.getHand();
        //noinspection DataFlowIssue
        if (stack != null && !stack.preHit(entity, (PlayerEntity) (Object) this)) ci.cancel();
    }
}
