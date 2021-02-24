package net.modificationstation.stationapi.mixin.server;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayerPacketHandler.class)
public class MixinServerPlayerPacketHandler {

    @Shadow
    private ServerPlayer serverPlayer;

    @ModifyConstant(method = {"method_1426(Lnet/minecraft/packet/Id14Packet;)V", "method_1422(Lnet/minecraft/packet/Id7Packet;)V"}, constant = @Constant(doubleValue = 36))
    private double getBlockReach(double originalReach) {
        return Math.pow(StationAPI.EVENT_BUS.post(new PlayerEvent.Reach(serverPlayer, HitType.TILE, Math.sqrt(originalReach))).currentReach, 2);
//        originalReach = Math.sqrt(originalReach);
//        Float defaultBlockReach = CustomReach.getDefaultBlockReach();
//        Float handBlockReach = CustomReach.getHandBlockReach();
//        if (defaultBlockReach != null)
//            originalReach = defaultBlockReach;
//        ItemInstance itemInstance = serverPlayer.getHeldItem();
//        if (itemInstance == null) {
//            if (handBlockReach != null)
//                originalReach = handBlockReach;
//        } else {
//            ItemBase itemBase = itemInstance.getType();
//            if (itemBase instanceof CustomReach) {
//                originalReach = ((CustomReach) itemBase).getCustomBlockReach(itemInstance, (float) originalReach);
//            }
//        }
//        return originalReach * originalReach;
    }

//    @ModifyConstant(method = "method_1422(Lnet/minecraft/packet/Id7Packet;)V", constant = @Constant(doubleValue = 36))
//    private double getEntityReach(double originalReach) {
//        return StationAPI.EVENT_BUS.post(new PlayerEvent.Reach(serverPlayer, HitType.ENTITY, originalReach)).currentReach;
////        originalReach = Math.sqrt(originalReach);
////        Double defaultEntityReach = CustomReach.getDefaultEntityReach();
////        Double handEntityReach = CustomReach.getHandEntityReach();
////        if (defaultEntityReach != null)
////            originalReach = defaultEntityReach;
////        ItemInstance itemInstance = serverPlayer.getHeldItem();
////        if (itemInstance == null) {
////            if (handEntityReach != null)
////                originalReach = handEntityReach;
////        } else {
////            ItemBase itemBase = itemInstance.getType();
////            if (itemBase instanceof CustomReach) {
////                originalReach = ((CustomReach) itemBase).getCustomEntityReach(itemInstance, originalReach);
////            }
////        }
////        return originalReach * originalReach;
//    }
}
