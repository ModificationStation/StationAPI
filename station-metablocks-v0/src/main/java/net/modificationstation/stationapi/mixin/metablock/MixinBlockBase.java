package net.modificationstation.stationapi.mixin.metablock;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.block.BlockStrengthPerMeta;
import net.modificationstation.stationapi.api.entity.player.StrengthOnMeta;
import net.modificationstation.stationapi.api.item.EffectiveOnMeta;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockBase.class)
public class MixinBlockBase implements BlockStrengthPerMeta {

    @Shadow
    @Final
    public Material material;

    @Shadow
    public float getHardness() {
        return 0;
    }

    @Override
    public float getBlockStrength(PlayerBase player, int meta) {
        float ret = getHardness(meta);
        if (ret < 0.0F)
            return 0.0F;
        return material.doesRequireTool() || player.getHeldItem() != null && ((EffectiveOnMeta) player.getHeldItem().getType()).isEffectiveOn((BlockBase) (Object) this, meta, player.getHeldItem()) ? ((StrengthOnMeta) player).getStrengh((BlockBase) (Object) this, meta) / ret / 30F : 1F / ret / 100F;
    }

    @Override
    public float getHardness(int meta) {
        return getHardness();
    }
}
