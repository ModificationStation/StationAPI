package net.modificationstation.stationapi.mixin.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.ItemEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.StationItem;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBase.class)
public class MixinItemBase implements StationItem {

    @Shadow @Final public int id;

    private final RegistryEntry.Reference<ItemBase> stationapi_registryEntry = ItemRegistry.INSTANCE.createEntry(ItemBase.class.cast(this));

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
        return StationAPI.EVENT_BUS.post(
                ItemEvent.TranslationKeyChanged.builder()
                        .item(ItemBase.class.cast(this))
                        .currentTranslationKey(name)
                        .build()
        ).currentTranslationKey;
    }

    @Override
    @Unique
    public int getLegacyID() {
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
        return ItemBase.class.cast(this);
    }

    @Override
    @Unique
    public RegistryEntry.Reference<ItemBase> getRegistryEntry() {
        return stationapi_registryEntry;
    }
}
