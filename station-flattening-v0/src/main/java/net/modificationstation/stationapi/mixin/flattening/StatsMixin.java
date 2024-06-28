package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.sugar.Local;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.StatRegistryEvent;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.StatRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Stats.class)
class StatsMixin {
    @Shadow public static Stat[] CRAFTED;

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/stat/Stats;FISH_CAUGHT:Lnet/minecraft/stat/Stat;",
                    opcode = Opcodes.PUTSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    private static void stationapi_afterStatRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new StatRegistryEvent());
    }

    @Inject(
            method = "initializeCraftedItemStats",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/ItemOrBlockStat;addStat()Lnet/minecraft/stat/Stat;",
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private static void stationapi_registerCraftedStats(
            CallbackInfo ci,
            @Local(index = 2) Integer index
    ) {
        val stat = CRAFTED[index];
        Registry.register(StatRegistry.INSTANCE, stat.id, Item.ITEMS[index].getRegistryEntry().registryKey().getValue().withSuffixedPath("_crafted"), stat);
    }

    @Inject(
            method = "method_749",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z"
            )
    )
    private static void stationapi_registerMinedStats(
            String string, int i, CallbackInfoReturnable<Stat[]> cir,
            @Local(index = 2) Stat[] statArray, @Local(index = 3) int index
    ) {
        val stat = statArray[index];
        Registry.register(StatRegistry.INSTANCE, stat.id, Block.BLOCKS[index].getRegistryEntry().registryKey().getValue().withSuffixedPath("_mined"), stat);
    }

    @Inject(
            method = "method_752",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/ItemOrBlockStat;addStat()Lnet/minecraft/stat/Stat;",
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private static void stationapi_registerUsedStats(
            Stat[] stats, String string, int i, int j, int k, CallbackInfoReturnable<Stat[]> cir,
            @Local(index = 5) int index
    ) {
        val stat = stats[index];
        Registry.register(StatRegistry.INSTANCE, stat.id, Item.ITEMS[index].getRegistryEntry().registryKey().getValue().withSuffixedPath("_used"), stat);
    }

    @Inject(
            method = "initializeBrokenItemStats",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/ItemOrBlockStat;addStat()Lnet/minecraft/stat/Stat;",
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private static void stationapi_registerBrokenStats(
            Stat[] stats, String string, int i, int j, int k, CallbackInfoReturnable<Stat[]> cir,
            @Local(index = 5) int index
    ) {
        val stat = stats[index];
        Registry.register(StatRegistry.INSTANCE, stat.id, Item.ITEMS[index].getRegistryEntry().registryKey().getValue().withSuffixedPath("_broken"), stat);
    }
}
