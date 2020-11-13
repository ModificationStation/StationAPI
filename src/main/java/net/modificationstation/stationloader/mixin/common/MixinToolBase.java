package net.modificationstation.stationloader.mixin.common;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolBase;
import net.modificationstation.stationloader.api.common.event.item.tool.EffectiveBlocksProvider;
import net.modificationstation.stationloader.mixin.common.accessor.ToolBaseAccessor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(ToolBase.class)
public class MixinToolBase {

    @Redirect(method = "<init>(IILnet/minecraft/item/tool/ToolMaterial;[Lnet/minecraft/block/BlockBase;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/item/tool/ToolBase;effectiveBlocksBase:[Lnet/minecraft/block/BlockBase;", opcode = Opcodes.PUTFIELD))
    private void getEffectiveBlocks(ToolBase toolBase, BlockBase[] value) {
        List<BlockBase> list = new ArrayList<>(Arrays.asList(value));
        EffectiveBlocksProvider.EVENT.getInvoker().getEffectiveBlocks(toolBase, ((ToolBaseAccessor) toolBase).getToolMaterial(), list);
        ((ToolBaseAccessor) toolBase).setEffectiveBlocksBase(list.toArray(new BlockBase[0]));
    }
}
