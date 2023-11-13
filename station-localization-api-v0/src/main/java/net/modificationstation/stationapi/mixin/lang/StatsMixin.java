package net.modificationstation.stationapi.mixin.lang;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.stat.ItemOrBlockStat;
import net.minecraft.stat.SimpleStat;
import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.impl.resource.language.DeferredTranslationKeyHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(Stats.class)
class StatsMixin {
    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resource/language/I18n;getTranslation(Ljava/lang/String;)Ljava/lang/String;"
            )
    )
    private static String stationapi_captureTranslationKey(
            String translationKey, Operation<String> original,
            @Share("translationKey") LocalRef<String> capturedTranslationKey
    ) {
        capturedTranslationKey.set(translationKey);
        return original.call(translationKey);
    }

    @ModifyExpressionValue(
            method = "<clinit>",
            at = {
                    @At(
                            value = "NEW",
                            target = "(ILjava/lang/String;)Lnet/minecraft/stat/SimpleStat;"
                    ),
                    @At(
                            value = "NEW",
                            target = "(ILjava/lang/String;Lnet/minecraft/stat/StatFormatter;)Lnet/minecraft/stat/SimpleStat;"
                    )
            }
    )
    private static SimpleStat stationapi_setCapturedTranslationKey(
            SimpleStat original,
            @Share("translationKey") LocalRef<String> translationKey
    ) {
        val capturedTranslationKey = translationKey.get();
        ((DeferredTranslationKeyHolder) original).stationapi_initTranslationKey(() -> I18n.getTranslation(capturedTranslationKey));
        return original;
    }

    @WrapOperation(
            method = {
                    "initializeCraftedItemStats",
                    "method_752",
                    "initializeBrokenItemStats"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;getTranslatedName()Ljava/lang/String;"
            )
    )
    private static String stationapi_captureFormatStringGetter(
            Item instance, Operation<String> original,
            @Share("formatStringGetter") LocalRef<Supplier<String>> capturedFormatStringGetter
    ) {
        capturedFormatStringGetter.set(() -> original.call(instance));
        return original.call(instance);
    }

    @WrapOperation(
            method = "method_749",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getTranslatedName()Ljava/lang/String;"
            )
    )
    private static String stationapi_captureFormatStringGetter(
            Block instance, Operation<String> original,
            @Share("formatStringGetter") LocalRef<Supplier<String>> capturedFormatStringGetter
    ) {
        capturedFormatStringGetter.set(() -> original.call(instance));
        return original.call(instance);
    }

    @WrapOperation(
            method = {
                    "initializeCraftedItemStats",
                    "method_749",
                    "method_752",
                    "initializeBrokenItemStats"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resource/language/I18n;getTranslation(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"
            )
    )
    private static String stationapi_captureTranslationKey(
            String translationKey, Object[] formatObjects, Operation<String> original,
            @Share("translationKey") LocalRef<String> capturedTranslationKey
    ) {
        capturedTranslationKey.set(translationKey);
        return original.call(translationKey, formatObjects);
    }

    @ModifyExpressionValue(
            method = {
                    "initializeCraftedItemStats",
                    "method_749",
                    "method_752",
                    "initializeBrokenItemStats"
            },
            at = @At(
                    value = "NEW",
                    target = "(ILjava/lang/String;I)Lnet/minecraft/stat/ItemOrBlockStat;"
            )
    )
    private static ItemOrBlockStat stationapi_setTranslationKey(
            ItemOrBlockStat original,
            @Share("translationKey") LocalRef<String> translationKey,
            @Share("formatStringGetter") LocalRef<Supplier<String>> formatStringGetter
    ) {
        val capturedTranslationKey = translationKey.get();
        val capturedFormatStringGetter = formatStringGetter.get();
        ((DeferredTranslationKeyHolder) original).stationapi_initTranslationKey(() -> I18n.getTranslation(capturedTranslationKey, capturedFormatStringGetter.get()));
        return original;
    }
}
