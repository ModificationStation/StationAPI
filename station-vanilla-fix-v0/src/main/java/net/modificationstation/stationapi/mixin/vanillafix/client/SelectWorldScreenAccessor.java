package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.world.storage.WorldSaveInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SelectWorldScreen.class)
public interface SelectWorldScreenAccessor {
    @Accessor
    List<WorldSaveInfo> getField_2436();
}
