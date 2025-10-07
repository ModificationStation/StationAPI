package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.stat.ItemOrBlockStat;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.StatRegistryEvent;
import net.modificationstation.stationapi.api.item.BlockItemForm;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.StatRegistry;
import net.modificationstation.stationapi.api.registry.sync.trackers.Int2ObjectMapTracker;
import net.modificationstation.stationapi.api.registry.sync.trackers.ObjectArrayTracker;
import net.modificationstation.stationapi.impl.stat.VanillaIdHolder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Stats.class)
class StatsMixin {
    @Shadow public static Stat[] CRAFTED;

    @Shadow public static Stat[] MINE_BLOCK;

    @Shadow public static Stat[] USED;

    @Shadow public static Stat[] BROKEN;

    @Shadow private static boolean hasExtendedItemStatsInitialized;

    @Shadow private static boolean hasBasicItemStatsInitialized;

    @Shadow protected static Map<Integer, Stat> ID_TO_STAT;

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/stat/Stats;ID_TO_STAT:Ljava/util/Map;",
                    opcode = Opcodes.PUTSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    private static void stationapi_syncMap(CallbackInfo ci) {
        Int2ObjectMapTracker.register(StatRegistry.INSTANCE, "Stats.ID_TO_STAT", ID_TO_STAT, true);
    }

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/stat/Stats;MINE_BLOCK:[Lnet/minecraft/stat/Stat;",
                    opcode = Opcodes.PUTSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    private static void stationapi_syncMined(CallbackInfo ci) {
        ObjectArrayTracker.register(BlockRegistry.INSTANCE, () -> MINE_BLOCK, array -> MINE_BLOCK = array);
    }

    @Inject(
            method = "initializeItemStats",
            at = @At("HEAD")
    )
    private static void stationapi_syncUsedAndBroken(CallbackInfo ci) {
        if (!hasExtendedItemStatsInitialized) {
            ObjectArrayTracker.register(ItemRegistry.INSTANCE, () -> USED, array -> USED = array);
            ObjectArrayTracker.register(ItemRegistry.INSTANCE, () -> BROKEN, array -> BROKEN = array);
        }
    }

    @Redirect(
            method = "initializeItemStats",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;BLOCKS:[Lnet/minecraft/block/Block;",
                    opcode = Opcodes.GETSTATIC,
                    args = "array=length",
                    ordinal = 0
            )
    )
    private static int stationapi_initAll(Block[] blocks) {
        return Item.ITEMS.length;
    }

    @Inject(
            method = "initializeExtendedItemStats",
            at = @At("HEAD")
    )
    private static void stationapi_syncUsedAndBrokenExtended(CallbackInfo ci) {
        if (!hasBasicItemStatsInitialized) {
            ObjectArrayTracker.register(ItemRegistry.INSTANCE, () -> USED, array -> USED = array);
            ObjectArrayTracker.register(ItemRegistry.INSTANCE, () -> BROKEN, array -> BROKEN = array);
        }
    }

    @WrapOperation(
            method = "initializeExtendedItemStats",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/stat/Stats;initItemsUsedStats([Lnet/minecraft/stat/Stat;Ljava/lang/String;III)[Lnet/minecraft/stat/Stat;"
            )
    )
    private static Stat[] stationapi_stopBlockUsedStats(Stat[] stats, String name, int id, int minId, int maxId, Operation<Stat[]> original) {
        return new Stat[maxId];
    }

    @Inject(
            method = "initializeCraftedItemStats",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/stat/Stats;CRAFTED:[Lnet/minecraft/stat/Stat;",
                    opcode = Opcodes.PUTSTATIC
            )
    )
    private static void stationapi_syncCrafted(CallbackInfo ci) {
        ObjectArrayTracker.register(ItemRegistry.INSTANCE, () -> CRAFTED, array -> CRAFTED = array);
    }

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
            method = "initBlocksMined",
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
            method = "initItemsUsedStats",
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

    @ModifyConstant(
            method = "initBlocksMined",
            constant = @Constant(intValue = 256)
    )
    private static int stationapi_getBlocksSize(int constant) {
        return Block.BLOCKS.length;
    }

    @ModifyConstant(
            method = {
                    "initializeItemStats",
                    "initializeExtendedItemStats",
                    "initializeCraftedItemStats",
                    "initItemsUsedStats",
                    "initializeBrokenItemStats"
            },
            constant = @Constant(intValue = 32000)
    )
    private static int stationapi_getItemsSize(int constant) {
        return Item.ITEMS.length;
    }

    @WrapOperation(
            method = {
                    "initializeCraftedItemStats",
                    "initBlocksMined",
                    "initItemsUsedStats",
                    "initializeBrokenItemStats"
            },
            at = @At(
                    value = "NEW",
                    target = "(ILjava/lang/String;I)Lnet/minecraft/stat/ItemOrBlockStat;"
            )
    )
    private static ItemOrBlockStat stationapi_replaceId(int id, String translationKey, int itemIdOrBlockId, Operation<ItemOrBlockStat> original) {
        final ItemOrBlockStat stat = original.call(StatRegistry.AUTO_ID, translationKey, itemIdOrBlockId);
        ((VanillaIdHolder) stat).setVanillaId(id);
        return stat;
    }

    @Redirect(
            method = "initItemsUsedStats",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;BLOCKS:[Lnet/minecraft/block/Block;",
                    opcode = Opcodes.GETSTATIC,
                    args = "array=length"
            )
    )
    private static int stationapi_fixBlockCheck(
            Block[] array,
            @Local(index = 5) int id
    ) {
        return Item.ITEMS[id] instanceof BlockItemForm ? id + 1 : id;
    }
}