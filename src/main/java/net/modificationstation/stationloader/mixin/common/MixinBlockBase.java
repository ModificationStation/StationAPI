package net.modificationstation.stationloader.mixin.common;

import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.common.block.BlockManager;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBase.class)
public class MixinBlockBase {

    @Shadow @Final public static BlockBase[] BY_ID;

    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
    private static void onBlockRegister(CallbackInfo ci) {
        BlockRegister.EVENT.getInvoker().registerBlocks();
    }

    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(I)Lnet/minecraft/item/PlaceableTileEntity;"))
    private static PlaceableTileEntity getBlockItem(int blockID) {
        return BlockManager.INSTANCE.getBlockItem(BY_ID[blockID + BY_ID.length]);
    }
}
