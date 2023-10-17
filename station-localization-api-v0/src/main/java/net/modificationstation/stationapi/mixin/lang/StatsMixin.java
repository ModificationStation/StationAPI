package net.modificationstation.stationapi.mixin.lang;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import lombok.val;
import net.minecraft.block.BlockBase;
import net.minecraft.client.StatEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemBase;
import net.minecraft.stat.RegisteringStat;
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
                    target = "Lnet/minecraft/client/resource/language/I18n;translate(Ljava/lang/String;)Ljava/lang/String;"
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
                            target = "(ILjava/lang/String;)Lnet/minecraft/stat/RegisteringStat;"
                    ),
                    @At(
                            value = "NEW",
                            target = "(ILjava/lang/String;Lnet/minecraft/stat/StatFormatter;)Lnet/minecraft/stat/RegisteringStat;"
                    )
            }
    )
    private static RegisteringStat stationapi_setCapturedTranslationKey(
            RegisteringStat original,
            @Share("translationKey") LocalRef<String> translationKey
    ) {
        val capturedTranslationKey = translationKey.get();
        ((DeferredTranslationKeyHolder) original).stationapi_initTranslationKey(() -> I18n.translate(capturedTranslationKey));
        return original;
    }

    @WrapOperation(
            method = {
                    "setupCrafting",
                    "setupUse",
                    "setupBreak"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemBase;getTranslatedName()Ljava/lang/String;"
            )
    )
    private static String stationapi_captureFormatStringGetter(
            ItemBase instance, Operation<String> original,
            @Share("formatStringGetter") LocalRef<Supplier<String>> capturedFormatStringGetter
    ) {
        capturedFormatStringGetter.set(() -> original.call(instance));
        return original.call(instance);
    }

    @WrapOperation(
            method = "setupMinedBlocks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTranslatedName()Ljava/lang/String;"
            )
    )
    private static String stationapi_captureFormatStringGetter(
            BlockBase instance, Operation<String> original,
            @Share("formatStringGetter") LocalRef<Supplier<String>> capturedFormatStringGetter
    ) {
        capturedFormatStringGetter.set(() -> original.call(instance));
        return original.call(instance);
    }

    @WrapOperation(
            method = {
                    "setupCrafting",
                    "setupMinedBlocks",
                    "setupUse",
                    "setupBreak"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resource/language/I18n;translate(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"
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
                    "setupCrafting",
                    "setupMinedBlocks",
                    "setupUse",
                    "setupBreak"
            },
            at = @At(
                    value = "NEW",
                    target = "(ILjava/lang/String;I)Lnet/minecraft/client/StatEntity;"
            )
    )
    private static StatEntity stationapi_setTranslationKey(
            StatEntity original,
            @Share("translationKey") LocalRef<String> translationKey,
            @Share("formatStringGetter") LocalRef<Supplier<String>> formatStringGetter
    ) {
        val capturedTranslationKey = translationKey.get();
        val capturedFormatStringGetter = formatStringGetter.get();
        ((DeferredTranslationKeyHolder) original).stationapi_initTranslationKey(() -> I18n.translate(capturedTranslationKey, capturedFormatStringGetter.get()));
        return original;
    }
}
