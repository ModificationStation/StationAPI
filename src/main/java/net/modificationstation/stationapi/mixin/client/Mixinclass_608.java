package net.modificationstation.stationapi.mixin.client;

import net.minecraft.block.BlockBase;
import net.minecraft.class_608;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.block.BlockStrengthPerMeta;
import net.modificationstation.stationapi.api.common.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_608.class)
public class Mixinclass_608 extends ClientInteractionManager {

    public Mixinclass_608(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(method = "getBlockReachDistance()F", at = @At("RETURN"), cancellable = true)
    private void getBlockReach(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue((float) StationAPI.EVENT_BUS.post(new PlayerEvent.Reach(minecraft.player, HitType.TILE, cir.getReturnValueF())).currentReach);
//        Float defaultBlockReach = CustomReach.getDefaultBlockReach();
//        Float handBlockReach = CustomReach.getHandBlockReach();
//        if (defaultBlockReach != null)
//            cir.setReturnValue(defaultBlockReach);
//        ItemInstance itemInstance = minecraft.player.getHeldItem();
//        if (itemInstance == null) {
//            if (handBlockReach != null)
//                cir.setReturnValue(handBlockReach);
//        } else {
//            ItemBase itemBase = itemInstance.getType();
//            if (itemBase instanceof CustomReach)
//                cir.setReturnValue(((CustomReach) itemBase).getCustomBlockReach(itemInstance, cir.getReturnValue()));
//        }
    }

    @Redirect(method = {"method_1707(IIII)V", "method_1721(IIII)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        return ((BlockStrengthPerMeta) blockBase).getBlockStrength(arg, arg.level.getTileMeta(i, j, k));
    }
}
