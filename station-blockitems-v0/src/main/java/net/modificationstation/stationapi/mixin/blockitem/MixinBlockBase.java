package net.modificationstation.stationapi.mixin.blockitem;

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
public class MixinBlockBase implements StationBlockItemsBlock {

    @Shadow
    @Final
    public static Block[] BY_ID;

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
    private static void eradicateNotchCode(Item[] array, int index, Item value) {} // it's set in the ItemBase constructor anyway

    @SuppressWarnings({"InvalidMemberReference", "InvalidInjectorMethodSignature", "MixinAnnotationTarget", "UnresolvedMixinReference"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Block;"
            )
    )
    private static BlockItem getBlockItem(int blockID) {
        Block block = BY_ID[blockID + ItemRegistry.ID_SHIFT];
        BlockItemFactoryEvent event = StationAPI.EVENT_BUS.post(
                BlockItemFactoryEvent.builder()
                        .block(block)
                        .currentFactory(BlockItem::new)
                        .build()
        );
        return event.isCanceled() ?
                BlockFormOnlyHandler.EMPTY_BLOCK_ITEM.get() :
                Registry.register(
                        ItemRegistry.INSTANCE,
                        BlockRegistry.INSTANCE.getId(block),
                        blockID < 0 ?
                                event.currentFactory.apply(blockID) :
                                BlockItemForm.of(
                                        () -> event.currentFactory.apply(ItemRegistry.AUTO_ID),
                                        block
                                )
                );
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
    public Block disableAutomaticBlockItemRegistration() {
        stationapi$disableAutomaticBlockItemRegistration = true;
        return Block.class.cast(this);
    }

    @Override
    @Unique
    public boolean isAutomaticBlockItemRegistrationDisabled() {
        return stationapi$disableAutomaticBlockItemRegistration;
    }
}
