package net.modificationstation.stationapi.mixin.block;

import net.minecraft.achievement.AchievementGUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AchievementGUID.class)
public interface AchievementGUIDAccessor {

    @Accessor("GUIDByID")
    Map<Integer, String> getGUIDByID();
}
