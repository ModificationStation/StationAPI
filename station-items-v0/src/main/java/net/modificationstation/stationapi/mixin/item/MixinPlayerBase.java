package net.modificationstation.stationapi.mixin.item;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.item.UseOnEntityFirst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerBase.class)
public abstract class MixinPlayerBase extends Living {

    public MixinPlayerBase(Level arg) {
        super(arg);
    }

    @Shadow public abstract ItemInstance getHeldItem();

    @Inject(method = "interactWith(Lnet/minecraft/entity/EntityBase;)V", at = @At(value = "HEAD"), cancellable = true)
    private void hijackInteractWith(EntityBase arg, CallbackInfo ci) {
        if (getHeldItem() != null && getHeldItem().getType() instanceof UseOnEntityFirst use && use.onUseOnEntityFirst(getHeldItem(), (PlayerBase) (Object) this, this.level, arg))
            ci.cancel();
    }
}
