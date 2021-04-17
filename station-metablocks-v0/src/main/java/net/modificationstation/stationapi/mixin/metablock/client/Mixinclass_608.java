package net.modificationstation.stationapi.mixin.metablock.client;

import net.minecraft.block.BlockBase;
import net.minecraft.class_608;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.block.BlockStrengthPerMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_608.class)
public class Mixinclass_608 extends ClientInteractionManager {

    public Mixinclass_608(Minecraft minecraft) {
        super(minecraft);
    }

//    @Inject(method = "getBlockReachDistance()F", at = @At("RETURN"), cancellable = true)
//    private void getBlockReach(CallbackInfoReturnable<Float> cir) {
//        cir.setReturnValue((float) StationAPI.EVENT_BUS.post(new PlayerEvent.Reach(minecraft.player, HitType.TILE, cir.getReturnValueF())).currentReach);
//    }

    @Redirect(method = {"method_1707(IIII)V", "method_1721(IIII)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        System.out.println("cunt");
        return ((BlockStrengthPerMeta) blockBase).getBlockStrength(arg, arg.level.getTileMeta(i, j, k));
    }
}
