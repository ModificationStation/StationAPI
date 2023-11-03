package net.modificationstation.stationapi.mixin.vanillafix.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.class_591;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

@Mixin(SelectWorldScreen.class)
public interface SelectWorldAccessor {

    @Accessor
    List<class_591> getWorlds();
}
