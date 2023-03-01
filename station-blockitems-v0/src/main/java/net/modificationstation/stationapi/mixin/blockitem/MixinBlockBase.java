package net.modificationstation.stationapi.mixin.blockitem;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockItemToggle;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.event.registry.BlockItemRegistryEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.impl.block.BlockFormOnlyHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBase.class)
public class MixinBlockBase implements BlockItemToggle<BlockBase> {

    @Shadow
    @Final
    public static BlockBase[] BY_ID;

    @Unique
    private boolean stationapi$disableAutomaticBlockItemRegistration;

    @SuppressWarnings({"InvalidInjectorMethodSignature", "MixinAnnotationTarget"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;byId:[Lnet/minecraft/item/ItemBase;",
                    opcode = Opcodes.GETSTATIC,
                    args = {
                            "array=set",
                            "fuzz=7"
                    },
                    ordinal = 8
            )
    )
    private static void eradicateNotchCode(ItemBase[] array, int index, ItemBase value) {} // it's set in the ItemBase constructor anyway

    @SuppressWarnings({"InvalidMemberReference", "InvalidInjectorMethodSignature", "MixinAnnotationTarget", "UnresolvedMixinReference"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Block;"
            )
    )
    private static Block getBlockItem(int blockID) {
        BlockBase block = BY_ID[blockID + BY_ID.length];
        BlockItemFactoryEvent event = StationAPI.EVENT_BUS.post(
                BlockItemFactoryEvent.builder()
                        .block(block)
                        .currentFactory(Block::new)
                        .build()
        );
        return event.isCanceled() ? BlockFormOnlyHandler.EMPTY_BLOCK_ITEM.get() : Registry.register(ItemRegistry.INSTANCE, BlockRegistry.INSTANCE.getId(block), event.currentFactory.apply(blockID));
    }

    @Inject(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;ALLOWS_GRASS_UNDER:[Z",
                    opcode = Opcodes.GETSTATIC,
                    shift = At.Shift.BEFORE
            )
    )
    private static void registerBlockItems(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new BlockItemRegistryEvent());
    }

    @Override
    @Unique
    public BlockBase disableAutomaticBlockItemRegistration() {
        stationapi$disableAutomaticBlockItemRegistration = true;
        return BlockBase.class.cast(this);
    }

    @Override
    @Unique
    public boolean isAutomaticBlockItemRegistrationDisabled() {
        return stationapi$disableAutomaticBlockItemRegistration;
    }
}
