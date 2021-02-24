package net.modificationstation.stationapi.mixin.common;

import net.minecraft.achievement.Achievement;
import net.minecraft.achievement.Achievements;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.achievement.AchievementRegisterEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Achievements.class)
public class MixinAchievements {

    @Shadow
    public static List<Achievement> ACHIEVEMENTS;

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/achievement/Achievements;FLY_PIG:Lnet/minecraft/achievement/Achievement;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
    private static void afterAchievementRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new AchievementRegisterEvent(ACHIEVEMENTS));
    }
}
