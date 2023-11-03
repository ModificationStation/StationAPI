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
public abstract class MixinPlayerBase extends LivingEntity {

    public MixinPlayerBase(World arg) {
        super(arg);
    }

    @Shadow public abstract ItemStack getHeldItem();

    @Inject(method = "interactWith(Lnet/minecraft/entity/EntityBase;)V", at = @At(value = "HEAD"), cancellable = true)
    private void hijackInteractWith(Entity arg, CallbackInfo ci) {
        if (getHeldItem() != null && getHeldItem().getItem() instanceof UseOnEntityFirst use && use.onUseOnEntityFirst(getHeldItem(), (PlayerEntity) (Object) this, this.world, arg))
            ci.cancel();
    }


    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack_preHit(Entity entity, CallbackInfo ci){
        ItemStack itemInstance = this.getHeldItem();
        if(itemInstance != null){
            if(!itemInstance.preHit(entity, PlayerEntity.class.cast(this))){
                ci.cancel();
            }
        }
    }
}
