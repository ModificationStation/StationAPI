package net.modificationstation.stationapi.mixin.flattening;

import lombok.val;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.item.IsItemSuitableForStateEvent;
import net.modificationstation.stationapi.api.event.item.ItemMiningSpeedMultiplierOnStateEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.item.StationFlatteningItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.util.Util;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Mixin(ItemStack.class)
abstract class ItemStackMixin implements StationFlatteningItemStack {
    @Shadow public int itemId;

    @Shadow public abstract Item getItem();

    @Unique
    private static final String STATION_ID = of(NAMESPACE, "id").toString();

    @Unique
    private static final Set<ItemStack> STATION_ITEM_STACKS = Util.make(
            Collections.newSetFromMap(new WeakHashMap<>()),
            stacks -> ItemRegistry.INSTANCE.getEventBus().register(Listener.<RegistryIdRemapEvent<ItemRegistry>>simple()
                    .listener(event -> {
                        val remap = event.state.getRawIdChangeMap();
                        stacks.forEach(
                                stack -> stack.itemId = remap.getOrDefault(stack.itemId, stack.itemId)
                        );
                    })
                    .build()
    ));
    
    @Inject(
            method = "<init>(Lnet/minecraft/block/Block;)V",
            at = @At("TAIL")
    )
    private void stationapi_onInitFromBlock(Block block, CallbackInfo info) {
        Item item = block.asItem();
        if (item != null) {
            this.itemId = item.id;
        }
    }
    
    @Inject(
            method = "<init>(Lnet/minecraft/block/Block;I)V",
            at = @At("TAIL")
    )
    private void stationapi_onInitFromBlock(Block block, int count, CallbackInfo info) {
        Item item = block.asItem();
        if (item != null) {
            this.itemId = item.id;
        }
    }
    
    @Inject(
            method = "<init>(Lnet/minecraft/block/Block;II)V",
            at = @At("TAIL")
    )
    private void stationapi_onInitFromBlock(Block block, int count, int meta, CallbackInfo info) {
        Item item = block.asItem();
        if (item != null) {
            this.itemId = item.id;
        }
    }

    @Redirect(
            method = "writeNbt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NbtCompound;putShort(Ljava/lang/String;S)V",
                    ordinal = 0
            )
    )
    private void stationapi_saveIdentifier(NbtCompound instance, String item, short i) {
        instance.putString(STATION_ID, ItemRegistry.INSTANCE.getId(itemId).orElseThrow().toString());
    }

    @Inject(
            method = "readNbt",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_loadIdentifier(NbtCompound par1, CallbackInfo ci) {
        String id = par1.getString(STATION_ID);
        if (id.isEmpty())
            LOGGER.warn("Attempted to load an item stack without Station Flattening ID! StationAPI will ignore this and accept the vanilla ID (" + itemId + "), but this should have been handled by DFU beforehand");
        else itemId = Objects.requireNonNull(ItemRegistry.INSTANCE.get(of(id))).id;
    }

    @Override
    @Unique
    public RegistryEntry.Reference<Item> getRegistryEntry() {
        return getItem().getRegistryEntry();
    }

    @Override
    @Unique
    public boolean isSuitableFor(BlockState state) {
        return StationAPI.EVENT_BUS.post(
                IsItemSuitableForStateEvent.builder()
                        .itemStack(ItemStack.class.cast(this))
                        .state(state)
                        .suitable(getItem().isSuitableFor(ItemStack.class.cast(this), state))
                        .build()
        ).suitable;
    }

    @Override
    @Unique
    public float getMiningSpeedMultiplier(BlockState state) {
        return StationAPI.EVENT_BUS.post(
                ItemMiningSpeedMultiplierOnStateEvent.builder()
                        .itemStack(ItemStack.class.cast(this))
                        .state(state)
                        .miningSpeedMultiplier(getItem().getMiningSpeedMultiplier(ItemStack.class.cast(this), state))
                        .build()
        ).miningSpeedMultiplier;
    }

    @Override
    @Unique
    public boolean isOf(Item item) {
        return this.getItem() == item;
    }

    @Inject(
            method = {
                    "<init>(III)V",
                    "<init>(Lnet/minecraft/nbt/NbtCompound;)V"
            },
            at = @At("RETURN")
    )
    private void stationapi_trackItemStack(CallbackInfo ci) {
        STATION_ITEM_STACKS.add((ItemStack) (Object) this);
    }
}
