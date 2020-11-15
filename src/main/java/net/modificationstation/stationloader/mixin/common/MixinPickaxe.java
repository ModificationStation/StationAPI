package net.modificationstation.stationloader.mixin.common;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.Pickaxe;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationloader.api.common.item.tool.PickaxeLevel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Pickaxe.class)
public class MixinPickaxe extends ToolBase implements PickaxeLevel {

    protected MixinPickaxe(int id, int j, ToolMaterial arg, BlockBase[] effectiveBlocks) {
        super(id, j, arg, effectiveBlocks);
    }

    @Override
    public int getToolLevel() {
        return toolMaterial.getMiningLevel();
    }
}
