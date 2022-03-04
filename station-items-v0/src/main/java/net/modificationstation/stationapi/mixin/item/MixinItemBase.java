package net.modificationstation.stationapi.mixin.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.ItemEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.serial.SerialIDHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBase.class)
public class MixinItemBase implements SerialIDHolder, ItemConvertible {

    @Shadow @Final public int id;

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/Stats;setupItemStats()V",
                    shift = At.Shift.BEFORE
            )
    )
    private static void afterItemRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new ItemRegistryEvent());
    }

    @ModifyVariable(
            method = "setTranslationKey(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private String getName(String name) {
        return StationAPI.EVENT_BUS.post(new ItemEvent.TranslationKeyChanged((ItemBase) (Object) this, name)).currentTranslationKey;
    }

    @Override
    @Unique
    public int getSerialID() {
        return id;
    }

    @ModifyConstant(
            method = "<init>(I)V",
            constant = @Constant(intValue = 256)
    )
    private int getBlocksSize(int constant) {
        return BlockRegistry.INSTANCE.getSize();
    }

    @Override
    @Unique
    public ItemBase asItem() {
        return (ItemBase) (Object) this;
    }
}
