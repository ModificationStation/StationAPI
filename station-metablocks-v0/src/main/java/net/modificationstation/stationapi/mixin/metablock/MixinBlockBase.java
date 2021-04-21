package net.modificationstation.stationapi.mixin.metablock;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.block.BlockHardnessPerMeta;
import net.modificationstation.stationapi.api.block.PlayerBlockHardnessPerMeta;
import net.modificationstation.stationapi.api.entity.player.CanPlayerRemoveMetaBlock;
import net.modificationstation.stationapi.api.entity.player.PlayerStrengthOnMetaBlock;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBase.class)
public abstract class MixinBlockBase implements BlockHardnessPerMeta, PlayerBlockHardnessPerMeta {

    @Shadow
    public float getHardness() {
        return 0;
    }

    @Shadow public abstract float getHardness(PlayerBase playerBase);

    @Shadow protected float hardness;

    @Override
    public float getHardness(PlayerBase player, int meta) {
        this.meta = meta;
        float hardness = getHardness(player);
        this.meta = null;
        return hardness;
    }

    @Redirect(method = "getHardness(Lnet/minecraft/entity/player/PlayerBase;)F", at = @At(value = "FIELD", target = "Lnet/minecraft/block/BlockBase;hardness:F", opcode = Opcodes.GETFIELD))
    private float redirectHardness(BlockBase blockBase) {
        return meta == null ? hardness : getHardness(meta);
    }

    @Redirect(method = "getHardness(Lnet/minecraft/entity/player/PlayerBase;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerBase;canRemoveBlock(Lnet/minecraft/block/BlockBase;)Z"))
    private boolean redirectCanRemoveBlock(PlayerBase playerBase, BlockBase arg) {
        return meta == null ? playerBase.canRemoveBlock(arg) : ((CanPlayerRemoveMetaBlock) playerBase).canRemoveBlock(arg, meta);
    }

    @Redirect(method = "getHardness(Lnet/minecraft/entity/player/PlayerBase;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerBase;getStrengh(Lnet/minecraft/block/BlockBase;)F"))
    private float redirectGetStrengh(PlayerBase playerBase, BlockBase arg) {
        return meta == null ? playerBase.getStrengh(arg) : ((PlayerStrengthOnMetaBlock) playerBase).getStrengh(arg, meta);
    }

    private Integer meta;

    @Override
    public float getHardness(int meta) {
        return getHardness();
    }
}
