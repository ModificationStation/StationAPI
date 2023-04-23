package net.modificationstation.stationapi.mixin.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.ItemEvent;
import net.modificationstation.stationapi.api.item.StationItem;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemBase.class)
public abstract class MixinItemBase implements StationItem {

    @Mutable
    @Shadow @Final public int id;

    @Shadow public abstract ItemBase setTranslationKey(String string);

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
    public ItemBase setTranslationKey(ModID modID, String translationKey) {
        return setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    public ItemBase setTranslationKey(Identifier translationKey) {
        return setTranslationKey(translationKey.toString());
    }
}
