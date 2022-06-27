package net.modificationstation.stationapi.mixin.blockitem;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockItemToggle;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.impl.block.BlockFormOnlyHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBase.class)
public class MixinBlockBase implements BlockItemToggle<BlockBase> {

    @Shadow
    @Final
    public static BlockBase[] BY_ID;

    @Unique
    private boolean stationapi$disableBlockItem;

    @SuppressWarnings({"InvalidMemberReference", "InvalidInjectorMethodSignature", "MixinAnnotationTarget", "UnresolvedMixinReference"})
    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Block;"
            )
    )
    private static Block getBlockItem(int blockID) {
        BlockItemFactoryEvent event = StationAPI.EVENT_BUS.post(
                BlockItemFactoryEvent.builder()
                        .block(BY_ID[blockID + BY_ID.length])
                        .currentFactory(Block::new)
                        .build()
        );
        return event.isCanceled() ? BlockFormOnlyHandler.EMPTY_BLOCK_ITEM.get() : event.currentFactory.apply(blockID);
    }

    @Override
    public BlockBase disableBlockItem() {
        stationapi$disableBlockItem = true;
        return BlockBase.class.cast(this);
    }

    @Override
    public boolean isBlockItemDisabled() {
        return stationapi$disableBlockItem;
    }
}
