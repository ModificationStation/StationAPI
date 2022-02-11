package net.modificationstation.stationapi.mixin.blockitem;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBase.class)
public class MixinBlockBase {

    @Shadow
    @Final
    public static BlockBase[] BY_ID;

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Block;"
            )
    )
    private static Block getBlockItem(int blockID) {
        return StationAPI.EVENT_BUS.post(new BlockItemFactoryEvent(BY_ID[blockID + BY_ID.length], Block::new)).currentFactory.apply(blockID);
    }
}
