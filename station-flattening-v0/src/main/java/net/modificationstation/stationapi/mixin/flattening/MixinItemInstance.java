package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.item.IsItemSuitableForStateEvent;
import net.modificationstation.stationapi.api.event.item.ItemMiningSpeedMultiplierOnStateEvent;
import net.modificationstation.stationapi.api.item.StationFlatteningItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Mixin(ItemInstance.class)
public abstract class MixinItemInstance implements StationFlatteningItemStack {

    @Shadow public int itemId;

    @Shadow public abstract ItemBase getType();

    @Unique
    private static final String STATION_ID = of(MODID, "id").toString();

    @Redirect(
            method = "toTag(Lnet/minecraft/util/io/CompoundTag;)Lnet/minecraft/util/io/CompoundTag;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/io/CompoundTag;put(Ljava/lang/String;S)V",
                    ordinal = 0
            )
    )
    private void saveIdentifier(CompoundTag instance, String item, short i) {
        instance.put(STATION_ID, ItemRegistry.INSTANCE.getIdByLegacyId(itemId).orElseThrow().toString());
    }

    @Inject(
            method = "fromTag(Lnet/minecraft/util/io/CompoundTag;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;itemId:I",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void loadIdentifier(CompoundTag par1, CallbackInfo ci) {
        String id = par1.getString(STATION_ID);
        if (!id.isEmpty())
            itemId = ItemRegistry.INSTANCE.getLegacyId(of(id)).orElseThrow();
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return StationAPI.EVENT_BUS.post(
                IsItemSuitableForStateEvent.builder()
                        .itemStack(ItemInstance.class.cast(this))
                        .state(state)
                        .suitable(getType().isSuitableFor(ItemInstance.class.cast(this), state))
                        .build()
        ).suitable;
    }

    @Override
    public float getMiningSpeedMultiplier(BlockState state) {
        return StationAPI.EVENT_BUS.post(
                ItemMiningSpeedMultiplierOnStateEvent.builder()
                        .itemStack(ItemInstance.class.cast(this))
                        .state(state)
                        .miningSpeedMultiplier(getType().getMiningSpeedMultiplier(ItemInstance.class.cast(this), state))
                        .build()
        ).miningSpeedMultiplier;
    }

    @Override
    public boolean isOf(ItemBase item) {
        return this.getType() == item;
    }
}
