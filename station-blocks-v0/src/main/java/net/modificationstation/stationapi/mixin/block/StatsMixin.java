package net.modificationstation.stationapi.mixin.block;

import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Stats.class)
class StatsMixin {
    @Redirect(
            method = "initializeItemStats",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/Stats;method_752([Lnet/minecraft/stat/Stat;Ljava/lang/String;III)[Lnet/minecraft/stat/Stat;"
            )
    )
    private static Stat[] stationapi_stop1(Stat[] statArray, String translationKey, int startId, int statAmount, int arrayLength) {
        return new Stat[Item.ITEMS.length];
    }

    @Redirect(
            method = "initializeItemStats",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/Stats;initializeBrokenItemStats([Lnet/minecraft/stat/Stat;Ljava/lang/String;III)[Lnet/minecraft/stat/Stat;"
            )
    )
    private static Stat[] stationapi_stop2(Stat[] statArray, String translationKey, int startId, int statAmount, int arrayLength) {
        return new Stat[Item.ITEMS.length];
    }

    @Redirect(
            method = "initializeExtendedItemStats",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/Stats;method_752([Lnet/minecraft/stat/Stat;Ljava/lang/String;III)[Lnet/minecraft/stat/Stat;"
            )
    )
    private static Stat[] stationapi_stop3(Stat[] statArray, String translationKey, int startId, int statAmount, int arrayLength) {
        return new Stat[Item.ITEMS.length];
    }

    @Redirect(
            method = "initializeExtendedItemStats",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/Stats;initializeBrokenItemStats([Lnet/minecraft/stat/Stat;Ljava/lang/String;III)[Lnet/minecraft/stat/Stat;"
            )
    )
    private static Stat[] stop4(Stat[] statArray, String translationKey, int startId, int statAmount, int arrayLength) {
        return new Stat[Item.ITEMS.length];
    }
}
