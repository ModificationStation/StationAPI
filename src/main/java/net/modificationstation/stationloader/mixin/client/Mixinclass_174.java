package net.modificationstation.stationloader.mixin.client;

import net.minecraft.class_174;
import net.minecraft.entity.EntityBase;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationloader.api.server.entity.CustomSpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_174.class)
public class Mixinclass_174 {

    @Shadow public EntityBase field_597;

    @Inject(method = "method_600", at = @At(value = "HEAD"), cancellable = true)
    private void getSpawnData(CallbackInfoReturnable<AbstractPacket> cir) {
        if (this.field_597 instanceof CustomSpawnData)
            cir.setReturnValue(((CustomSpawnData) field_597).getSpawnData());
    }
}
