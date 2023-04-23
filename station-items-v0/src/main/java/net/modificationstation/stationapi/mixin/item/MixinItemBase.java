package net.modificationstation.stationapi.mixin.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.ItemEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.StationItem;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBase.class)
public abstract class MixinItemBase implements StationItem {

    @Mutable
    @Shadow @Final public int id;

    @Shadow public abstract ItemBase setTranslationKey(String string);

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
                        .translationKeyOverride(name)
                        .build()
        ).translationKeyOverride;
    }

    @Override
    @Unique
    public final void setRawId(int rawId) {
        id = rawId;
    }

    @ModifyConstant(
            method = "<init>(I)V",
            constant = @Constant(intValue = 256)
    )
    private int getBlocksSize(int constant) {
        return BlockBase.BY_ID.length;
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

    @Override
    public ItemBase setTranslationKey(ModID modID, String translationKey) {
        return setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    public ItemBase setTranslationKey(Identifier translationKey) {
        return setTranslationKey(translationKey.toString());
    }
}
