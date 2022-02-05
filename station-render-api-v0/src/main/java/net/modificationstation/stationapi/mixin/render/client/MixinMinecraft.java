package net.modificationstation.stationapi.mixin.render.client;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.WindowProvider;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.lang.reflect.*;
import java.util.function.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

@Mixin(Minecraft.class)
@net.fabricmc.api.Environment(EnvType.CLIENT)
public class MixinMinecraft implements WindowProvider {

    @Shadow private boolean isFullscreen;
    @Shadow public int actualWidth;
    @Shadow public int actualHeight;
    @Shadow public Canvas canvas;
    @Unique
    private long monitor;
    @Unique @Getter
    private long window;

    @Inject(
            method = "init()V",
            at = @At("HEAD")
    )
    private void trickInit(CallbackInfo ci) {
        Object theTrickster = new Object();
        Field canvasField = ((Supplier<Field>) () -> {
            for (Field field : Minecraft.class.getDeclaredFields())
                if (field.getType() == Canvas.class && (field.getModifiers() & ~Modifier.PUBLIC) == 0)
                    return field;
            throw new IllegalStateException("Bruh");
        }).get();
        long theFool = UnsafeProvider.theUnsafe.objectFieldOffset(canvasField);
        UnsafeProvider.theUnsafe.putObject(this, theFool, theTrickster);
    }

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/awt/Canvas;getGraphics()Ljava/awt/Graphics;",
                    remap = false
            )
    )
    private Graphics backstabJvm(Canvas instance) {
        return null;
    }

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/Display;setParent(Ljava/awt/Canvas;)V",
                    remap = false
            )
    )
    private void bamboozleLwjgl2(Canvas canvas) {
        glfwInit();
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        if (isFullscreen) {
            monitor = glfwGetPrimaryMonitor();
            GLFWVidMode vidMode = glfwGetVideoMode(monitor);
            actualWidth = vidMode == null ? 0 : vidMode.width();
            actualHeight = vidMode == null ? 0 : vidMode.height();
            if (actualWidth <= 0)
                actualWidth = 1;
            if (actualHeight <= 0)
                actualHeight = 1;
        }
    }

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V",
                    remap = false
            )
    )
    private void setTitle(String s) {
        window = glfwCreateWindow(actualWidth, actualHeight, s, monitor, 0);
    }

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/Display;create()V",
                    remap = false
            )
    )
    private void createDisplay() {
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwShowWindow(window);
    }

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/input/Controllers;create()V",
                    remap = false
            )
    )
    private void createControllers() {

    }

    @Inject(method = "init()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;textureManager:Lnet/minecraft/client/texture/TextureManager;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void textureManagerInit(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new TextureRegisterEvent());
    }

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;addTextureBinder(Lnet/minecraft/client/render/TextureBinder;)V"
            )
    )
    private void stopVanillaTextureBinders(TextureManager textureManager, TextureBinder arg) {
    }
}
