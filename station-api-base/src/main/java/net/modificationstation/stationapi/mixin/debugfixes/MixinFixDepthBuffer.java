package net.modificationstation.stationapi.mixin.debugfixes;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Minecraft.class, priority = 0)
public class MixinFixDepthBuffer {

//    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create()V", remap = false), require = 0)
//    private void fixDepthBuffer() throws LWJGLException {
//        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
//            PixelFormat pixelformat = new PixelFormat();
//            pixelformat = pixelformat.withDepthBits(24);
//            Display.create(pixelformat);
//        } else {
//            Display.create();
//        }
//    }
}
