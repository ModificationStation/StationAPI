package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.EnumOperatingSystems;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Invoker("getOperatingSystem")
    static EnumOperatingSystems stationapi$getOperatingSystem() {
        return Util.assertMixin();
    }
}
