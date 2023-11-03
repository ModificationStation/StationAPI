package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.StationFlatteningItem;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.sync.trackers.ObjectArrayTracker;
import net.modificationstation.stationapi.api.registry.sync.trackers.RemappableEntryArrayTracker;
import net.modificationstation.stationapi.api.registry.sync.trackers.vanilla.BlockItemTracker;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public abstract class MixinItemBase implements StationFlatteningItem {

    @Shadow public abstract boolean isEffectiveOn(Block arg);

    @Shadow public abstract float getStrengthOnBlock(ItemStack arg, Block arg2);

    @Shadow public static Item[] byId;

    @Mutable
    @Shadow @Final public int id;

    private RegistryEntry.Reference<Item> stationapi_registryEntry;

    @Override
    @Unique
    public RegistryEntry.Reference<Item> getRegistryEntry() {
        return stationapi_registryEntry;
    }

    @Override
    @Unique
    public final void setRawId(int rawId) {
        id = rawId;
    }

    @Override
    @Unique
    public Item asItem() {
        return Item.class.cast(this);
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
    public boolean isSuitableFor(ItemStack itemStack, BlockState state) {
        return isEffectiveOn(state.getBlock());
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack itemStack, BlockState state) {
        return getStrengthOnBlock(itemStack, state.getBlock());
    }

    @Inject(
            method = "<clinit>",
            at = @At("HEAD")
    )
    private static void setupRegistry(CallbackInfo ci) {
        ItemRegistry registry = ItemRegistry.INSTANCE;
        RemappableEntryArrayTracker.register(registry, () -> byId, array -> byId = array);
        BlockItemTracker.register(registry);
    }

    @ModifyVariable(
            method = "<init>",
            index = 1,
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=64",
                    shift = At.Shift.BY,
                    by = -2
            ),
            argsOnly = true
    )
    private int ensureCapacity(int rawId) {
        //noinspection DataFlowIssue
        rawId = (stationapi_registryEntry = ItemRegistry.INSTANCE.createReservedEntry(rawId + ItemRegistry.ID_SHIFT, (Item) (Object) this)).reservedRawId();
        // unfortunately, this array is accessed
        // too early for the tracker to resize it,
        // so we have to do it manually here
        if (ObjectArrayTracker.shouldGrow(byId, rawId)) byId = ObjectArrayTracker.grow(byId, rawId);
        return ItemRegistry.SHIFTED_ID.get(rawId);
    }
}
