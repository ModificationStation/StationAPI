package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.StationFlatteningItem;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.sync.trackers.ObjectArrayTracker;
import net.modificationstation.stationapi.api.registry.sync.trackers.RemappableEntryArrayTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBase.class)
public abstract class MixinItemBase implements StationFlatteningItem {

    @Shadow public abstract boolean isEffectiveOn(BlockBase arg);

    @Shadow public abstract float getStrengthOnBlock(ItemInstance arg, BlockBase arg2);

    @Shadow public static ItemBase[] byId;

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
