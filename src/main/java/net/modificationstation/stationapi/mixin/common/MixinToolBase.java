package net.modificationstation.stationapi.mixin.common;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.event.item.tool.EffectiveBlocksProvider;
import net.modificationstation.stationapi.api.common.item.tool.ToolLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(ToolBase.class)
public class MixinToolBase implements ToolLevel {

    @Shadow
    protected ToolMaterial toolMaterial;
    @Shadow
    private BlockBase[] effectiveBlocksBase;

    @Inject(method = "<init>(IILnet/minecraft/item/tool/ToolMaterial;[Lnet/minecraft/block/BlockBase;)V", at = @At("RETURN"))
    private void getEffectiveBlocks(int id, int j, ToolMaterial arg, BlockBase[] effectiveBlocks, CallbackInfo ci) {
        List<BlockBase> list = new ArrayList<>(Arrays.asList(effectiveBlocksBase));
        EffectiveBlocksProvider.EVENT.getInvoker().getEffectiveBlocks((ToolBase) (Object) this, toolMaterial, list);
        effectiveBlocksBase = list.toArray(new BlockBase[0]);
    }

    @Override
    public int getToolLevel() {
        return toolMaterial.getMiningLevel();
    }

    @Override
    public ToolMaterial getMaterial() {
        return toolMaterial;
    }
}
