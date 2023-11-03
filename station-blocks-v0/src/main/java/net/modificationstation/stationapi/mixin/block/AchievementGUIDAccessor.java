package net.modificationstation.stationapi.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import net.minecraft.class_404;

@Mixin(class_404.class)
public interface AchievementGUIDAccessor {

    @Accessor("GUIDByID")
    Map<Integer, String> getGUIDByID();
}
