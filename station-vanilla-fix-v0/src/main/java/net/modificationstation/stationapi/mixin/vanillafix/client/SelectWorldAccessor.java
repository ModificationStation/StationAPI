package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.minecraft.client.gui.screen.menu.SelectWorld;
import net.minecraft.level.storage.LevelMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SelectWorld.class)
public interface SelectWorldAccessor {

    @Accessor
    List<LevelMetadata> getWorlds();
}
