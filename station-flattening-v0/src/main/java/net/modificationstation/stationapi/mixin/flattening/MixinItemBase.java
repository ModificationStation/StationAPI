package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.StationFlatteningItem;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.sync.trackers.ObjectArrayTracker;
import net.modificationstation.stationapi.api.registry.sync.trackers.RemappableEntryArrayTracker;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBase.class)
public abstract class MixinItemBase implements StationFlatteningItem {

    @Shadow public abstract boolean isEffectiveOn(BlockBase arg);

    @Shadow public abstract float getStrengthOnBlock(ItemInstance arg, BlockBase arg2);

    @Shadow public static ItemBase[] byId;

    @Mutable
    @Shadow @Final public int id;

    private final RegistryEntry.Reference<ItemBase> stationapi_registryEntry = ItemRegistry.INSTANCE.createEntry(ItemBase.class.cast(this));

    @Override
    @Unique
    public RegistryEntry.Reference<ItemBase> getRegistryEntry() {
        return stationapi_registryEntry;
    }

    @Override
    @Unique
    public final void setRawId(int rawId) {
        id = rawId;
    }

    @Override
    @Unique
    public ItemBase asItem() {
        return ItemBase.class.cast(this);
    }

    @ModifyConstant(
            method = "<init>(I)V",
            constant = @Constant(intValue = 256)
    )
    private int getBlocksSize(int constant) {
        return BlockBase.BY_ID.length;
    }

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

    @Override
    public boolean isSuitableFor(ItemInstance itemStack, BlockState state) {
        return isEffectiveOn(state.getBlock());
    }

    @Override
    public float getMiningSpeedMultiplier(ItemInstance itemStack, BlockState state) {
        return getStrengthOnBlock(itemStack, state.getBlock());
    }

    @Inject(
            method = "<clinit>",
            at = @At("HEAD")
    )
    private static void setupRegistry(CallbackInfo ci) {
        ItemRegistry registry = ItemRegistry.INSTANCE;
        RemappableEntryArrayTracker.register(registry, () -> byId, array -> byId = array);
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Object;<init>()V",
                    shift = At.Shift.AFTER
            )
    )
    private void ensureCapacity(int rawId, CallbackInfo ci) {
        // unfortunately, this array is accessed
        // too early for the tracker to resize it,
        // so we have to do it manually here
        rawId += BlockBase.BY_ID.length;
        if (ObjectArrayTracker.shouldGrow(byId, rawId)) byId = ObjectArrayTracker.grow(byId, rawId);
    }
}
