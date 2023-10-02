package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.tool.StationToolItem;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolBase.class)
public class MixinToolBase extends ItemBase implements StationToolItem, CustomTooltipProvider {

    // TODO : Clean this vanilla mess up
    // TODO : Per tool attack damage ?

    protected MixinToolBase(int i) {
        super(i);
    }

    @Shadow protected ToolMaterial toolMaterial;

    @Shadow private int field_2714;
    @Shadow private float field_2713;
    @Unique
    private TagKey<BlockBase> stationapi_effectiveBlocks;

    @Override
    public void setEffectiveBlocks(TagKey<BlockBase> effectiveBlocks) {
        stationapi_effectiveBlocks = effectiveBlocks;
    }

    @Override
    public TagKey<BlockBase> getEffectiveBlocks(ItemInstance itemInstance) {
        return stationapi_effectiveBlocks;
    }

    @Override
    public ToolMaterial getMaterial(ItemInstance itemInstance) {
        return toolMaterial;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemInstance itemStack, BlockState state) {
        if(this.isSuitableFor(itemStack, state)){
            return this.toolMaterial.getMiningSpeed();
        }
        return 1F;
    }

    @Override
    public boolean isSuitableFor(ItemInstance itemStack, BlockState state) {
        return state.isIn(stationapi_effectiveBlocks) && this.toolMaterial.getMiningLevel() >= state.getBlock().getMiningLevel();
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        return new String[]{
                originalTooltip,
                "Durability : " + (itemInstance.getDurability()-itemInstance.getDamage()) + "/" + itemInstance.getDurability(),
                "Attack Damage : " + this.field_2714,
                "Mining Speed : " + this.field_2713,
                "Mining Level : " + this.toolMaterial.getMiningLevel()
        };
    }
}
