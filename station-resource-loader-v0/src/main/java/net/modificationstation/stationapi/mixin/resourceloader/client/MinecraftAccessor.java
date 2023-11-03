package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Accessor
    Timer getTickTimer();

    @Invoker
    void invokePrintOpenGLError(String location);
}
