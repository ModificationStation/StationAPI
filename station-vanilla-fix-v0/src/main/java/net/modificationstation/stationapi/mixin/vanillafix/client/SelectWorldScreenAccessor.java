package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.minecraft.class_591;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SelectWorldScreen.class)
public interface SelectWorldScreenAccessor {
    @Accessor
    List<class_591> getField_2436();
}
