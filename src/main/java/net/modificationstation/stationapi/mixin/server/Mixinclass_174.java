package net.modificationstation.stationapi.mixin.server;

import net.minecraft.entity.EntityBase;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.server.network.TrackedEntity;
import net.modificationstation.stationapi.api.server.entity.CustomSpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrackedEntity.class)
public class Mixinclass_174 {

    @Shadow
    public EntityBase field_597;

    @Inject(method = "method_600", at = @At(value = "HEAD"), cancellable = true)
    private void getSpawnData(CallbackInfoReturnable<AbstractPacket> cir) {
        if (this.field_597 instanceof CustomSpawnData)
            cir.setReturnValue(((CustomSpawnData) field_597).getSpawnData());
    }
}
