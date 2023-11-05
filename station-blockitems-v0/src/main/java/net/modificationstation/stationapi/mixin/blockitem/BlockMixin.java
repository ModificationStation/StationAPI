package net.modificationstation.stationapi.mixin.blockitem;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.StationBlockItemsBlock;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.event.registry.BlockItemRegistryEvent;
import net.modificationstation.stationapi.api.item.BlockItemForm;
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

@Mixin(Block.class)
class BlockMixin implements StationBlockItemsBlock {
    @Shadow
    @Final
    public static Block[] BLOCKS;

    @Unique
    private boolean stationapi$disableAutoItemRegistration;

    @SuppressWarnings({"InvalidInjectorMethodSignature", "MixinAnnotationTarget"})
    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Item;ITEMS:[Lnet/minecraft/item/Item;",
                    opcode = Opcodes.GETSTATIC,
                    args = {
                            "array=set",
                            "fuzz=7"
                    },
                    ordinal = 8
            )
    )
    private static void stationapi_eradicateNotchCode(Item[] array, int index, Item value) {} // it's set in the Item constructor anyway

    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/BlockItem;<init>(I)V"
            )
    )
    private static BlockItem stationapi_getBlockItem(int blockId, Operation<BlockItem> currentFactory) {
        Block block = BLOCKS[blockId + ItemRegistry.ID_SHIFT];
        BlockItemFactoryEvent event = StationAPI.EVENT_BUS.post(
                BlockItemFactoryEvent.builder()
                        .block(block)
                        .currentFactory(currentFactory::call)
                        .build()
        );
        return event.isCanceled() ?
                BlockFormOnlyHandler.EMPTY_BLOCK_ITEM.get() :
                Registry.register(
                        ItemRegistry.INSTANCE,
                        BlockRegistry.INSTANCE.getId(block),
                        blockId < 0 ?
                                event.currentFactory.apply(blockId) :
                                BlockItemForm.of(
                                        () -> event.currentFactory.apply(ItemRegistry.AUTO_ID),
                                        block
                                )
                );
    }

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;BLOCKS_ALLOW_VISION:[Z",
                    opcode = Opcodes.GETSTATIC,
                    shift = At.Shift.BEFORE
            )
    )
    private static void stationapi_registerBlockItems(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new BlockItemRegistryEvent());
    }

    @Override
    @Unique
    public Block disableAutoItemRegistration() {
        stationapi$disableAutoItemRegistration = true;
        return Block.class.cast(this);
    }

    @Override
    @Unique
    public boolean isAutoItemRegistrationDisabled() {
        return stationapi$disableAutoItemRegistration;
    }
}
