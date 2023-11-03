package net.modificationstation.stationapi.api.client.resource;

import cyclops.control.Option;
import lombok.Getter;
import lombok.val;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_564;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.resource.CompositeResourceReload;
import net.modificationstation.stationapi.api.resource.ResourceReload;
import net.modificationstation.stationapi.api.resource.ResourceReloader;
import net.modificationstation.stationapi.api.tick.TickScheduler;
import net.modificationstation.stationapi.api.util.profiler.ProfileResult;
import net.modificationstation.stationapi.impl.client.resource.ReloadScreenApplicationExecutor;
import net.modificationstation.stationapi.impl.client.resource.ReloadScreenManagerImpl;
import net.modificationstation.stationapi.impl.client.resource.ReloadScreenTessellatorHolder;
import net.modificationstation.stationapi.mixin.resourceloader.client.MinecraftAccessor;
import net.modificationstation.stationapi.mixin.resourceloader.client.TessellatorAccessor;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.SharedDrawable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import static cyclops.control.Option.none;
import static cyclops.control.Option.some;
import static cyclops.function.FluentFunctions.expression;
import static org.lwjgl.opengl.GL11.*;

public class ReloadScreenManager {
    @Getter
    private static @NotNull Option<Thread> thread = none();
    @Getter
    private static @NotNull Option<ResourceReload> currentReload = none();
    static ReloadScreen reloadScreen;
    static final List<String> LOCATIONS = new CopyOnWriteArrayList<>();
    private static final Executor EMPTY_EXECUTOR = command -> {};
    private static @NotNull Executor applicationExecutor = EMPTY_EXECUTOR;

    public static void pushLocation(String customLocation) {
        LOCATIONS.add(customLocation);
    }

    public static void pushLocation(ResourceReloader resourceReloader, String formatString, String location) {
        pushLocation(ProfileResult.getHumanReadableName(formatString.formatted(location.replaceAll("^root", resourceReloader.getName()))));
    }

    public static void openEarly() throws LWJGLException {
        ReloadScreenManagerImpl.isMinecraftDone = false;
        applicationExecutor = ReloadScreenApplicationExecutor.INSTANCE;
        currentReload = some(new CompositeResourceReload());
        //noinspection deprecation
        thread = some(new Thread(expression(ReloadScreenManager::onStartup).partiallyApply(
                (Minecraft) FabricLoader.getInstance().getGameInstance(),
                new SharedDrawable(Display.getDrawable())
        )::get));
        thread.peek(Thread::start);
    }

    public static void open() {
        applicationExecutor = TickScheduler.CLIENT_RENDER_END::distributed;
        currentReload = some(new CompositeResourceReload());
        TickScheduler.CLIENT_RENDER_START.immediate(
                () -> {
                    //noinspection deprecation
                    val minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
                    val parentScreen = minecraft.currentScreen;
                    val reloadScreen = new ReloadScreen(
                            parentScreen,
                            expression(minecraft::openScreen).applyLazy(parentScreen)::apply,
                            Tessellator.INSTANCE
                    );
                    minecraft.openScreen(reloadScreen);
                }
        );
    }

    public static boolean isReloadStarted() {
        return reloadScreen != null && reloadScreen.isReloadStarted();
    }

    public static boolean isReloadComplete() {
        return isReloadStarted() && currentReload.isPresent() && currentReload.orElse(null/*safe*/).isComplete();
    }

    public static @NotNull Executor getApplicationExecutor() {
        return applicationExecutor;
    }

    static void onFinish() {
        reloadScreen = null;
        LOCATIONS.clear();
        applicationExecutor = ReloadScreenManager.EMPTY_EXECUTOR;
        currentReload = none();
    }

    private static void onStartup(
            final Minecraft minecraft,
            final Drawable drawable
    ) {
        try {
            drawable.makeCurrent();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
        val done = new AtomicBoolean();
        val reloadScreen = new ReloadScreen(
                minecraft.currentScreen,
                expression(done::set).applyLazy(true)::apply,
                ReloadScreenTessellatorHolder.reloadScreenTessellator = TessellatorAccessor.stationapi_create(48)
        );
        val screenScaler = new class_564(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);
        val width = screenScaler.getScaledWidth();
        val height = screenScaler.getScaledHeight();
        reloadScreen.init(minecraft, width, height);
        reloadScreen.setTextRenderer(new TextRenderer(minecraft.options, "/font/default.png", minecraft.textureManager));
        val timer = ((MinecraftAccessor) minecraft).getTickTimer();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, screenScaler.scaledWidth, screenScaler.scaledHeight, 0.0, 1000.0, 3000.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(0.0f, 0.0f, -2000.0f);
        glViewport(0, 0, minecraft.displayWidth, minecraft.displayHeight);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_FOG);
        while (!done.get()) {
            while (true) if (!Mouse.next()) break;
            while (true) if (!Keyboard.next()) break;
            val f = timer.field_2370;
            timer.method_1853();
            timer.field_2370 = f;
            val mouseX = Mouse.getX() * width / minecraft.displayWidth;
            val mouseY = height - Mouse.getY() * height / minecraft.displayHeight - 1;
            reloadScreen.render(mouseX, mouseY, timer.field_2370);
            Display.update();
        }
        glDisable(GL_LIGHTING);
        glDisable(GL_FOG);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.1f);
        try {
            drawable.releaseContext();
            drawable.destroy();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
        thread = none();
    }
}
