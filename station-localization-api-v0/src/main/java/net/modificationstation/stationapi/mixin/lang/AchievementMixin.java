package net.modificationstation.stationapi.mixin.lang;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import lombok.val;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.resource.language.TranslationInvalidationEvent;
import net.modificationstation.stationapi.impl.resource.language.DeferredTranslationKeyHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Achievement.class)
class AchievementMixin {
    @Mutable
    @Shadow @Final private String translationKey;

    @WrapOperation(
            method = "<init>(ILjava/lang/String;IILnet/minecraft/item/ItemStack;Lnet/minecraft/achievement/Achievement;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resource/language/I18n;getTranslation(Ljava/lang/String;)Ljava/lang/String;",
                    ordinal = 0
            )
    )
    private static String stationapi_captureTranslationKey(
            String translationKey, Operation<String> original,
            @Share("translationKey") LocalRef<String> capturedTranslationKey
    ) {
        capturedTranslationKey.set(translationKey);
        return original.call(translationKey);
    }

    @WrapOperation(
            method = "<init>(ILjava/lang/String;IILnet/minecraft/item/ItemStack;Lnet/minecraft/achievement/Achievement;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resource/language/I18n;getTranslation(Ljava/lang/String;)Ljava/lang/String;",
                    ordinal = 1
            )
    )
    private String stationapi_captureDescriptionTranslationKey(String descriptionTranslationKey, Operation<String> original) {
        StationAPI.EVENT_BUS.register(
                Listener.<TranslationInvalidationEvent>simple()
                        .phase(StationAPI.INTERNAL_PHASE)
                        .listener(event -> translationKey = I18n.getTranslation(descriptionTranslationKey))
                        .build()
        );
        return original.call(descriptionTranslationKey);
    }

    @Inject(
            method = "<init>(ILjava/lang/String;IILnet/minecraft/item/ItemStack;Lnet/minecraft/achievement/Achievement;)V",
            at = @At("RETURN")
    )
    private void stationapi_setCapturedTranslationKey(
            int string, String j, int k, int arg, ItemStack arg2, Achievement par6, CallbackInfo ci,
            @Share("translationKey") LocalRef<String> translationKey
    ) {
        val capturedTranslationKey = translationKey.get();
        ((DeferredTranslationKeyHolder) this).stationapi_initTranslationKey(() -> I18n.getTranslation(capturedTranslationKey));
    }
}
