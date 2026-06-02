package net.modificationstation.stationapi.api.client.resource;

import lombok.Getter;
import lombok.val;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.ScreenScaler;
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
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static org.lwjgl.opengl.GL11.*;

public class ReloadScreenManager {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Getter
    private static @NotNull Optional<Thread> thread = Optional.empty();
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Getter
    private static @NotNull Optional<ResourceReload> currentReload = Optional.empty();
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

    public static void openEarly() throws Exception {
        ReloadScreenManagerImpl.isMinecraftDone = false;
        applicationExecutor = ReloadScreenApplicationExecutor.INSTANCE;
        currentReload = Optional.of(new CompositeResourceReload());
        //noinspection deprecation
        final Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();

        // Use starac's EarlyRenderLoop if available (LWJGL3 with proper event handling)
        if (LWJGLHelper.hasEarlyRenderLoop()) {
            LOGGER.info("Using starac EarlyRenderLoop for loading screen");
            onStartupWithEarlyRenderLoop(minecraft);
            return;
        }

        // Fall back to LWJGL2 threaded approach
        if (LWJGLHelper.isLWJGL2()) {
            final Object drawable = LWJGLHelper.createSharedDrawable();
            thread = Optional.of(new Thread(() -> onStartupThreaded(minecraft, drawable)));
            thread.ifPresent(Thread::start);
            return;
        }

        // No compatible approach available - skip early loading screen
        LOGGER.warn("No compatible early loading screen implementation available");
    }

    public static void open() {
        applicationExecutor = TickScheduler.CLIENT_RENDER_END::distributed;
        currentReload = Optional.of(new CompositeResourceReload());
        TickScheduler.CLIENT_RENDER_START.immediate(
                () -> {
                    //noinspection deprecation
                    val minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
                    val parentScreen = minecraft.currentScreen;
                    val reloadScreen = new ReloadScreen(
                            parentScreen,
                            () -> minecraft.setScreen(parentScreen),
                            Tessellator.INSTANCE
                    );
                    minecraft.setScreen(reloadScreen);
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
        currentReload = Optional.empty();
    }

    // For single-threaded mode (starac): flag and stored state
    private static boolean usingEarlyRenderLoop = false;
    private static ReloadScreen earlyRenderLoopScreen = null;
    private static AtomicBoolean earlyRenderLoopDone = null;

    /**
     * Sets up state for single-threaded early render loop.
     * Returns immediately - actual render loop runs in runEarlyRenderLoop().
     */
    private static void onStartupWithEarlyRenderLoop(final Minecraft minecraft) {
        usingEarlyRenderLoop = true;
        earlyRenderLoopDone = new AtomicBoolean(false);
        earlyRenderLoopScreen = new ReloadScreen(
                minecraft.currentScreen,
                () -> earlyRenderLoopDone.set(true),
                ReloadScreenTessellatorHolder.reloadScreenTessellator = TessellatorAccessor.stationapi_create(48)
        );
        val screenScaler = new ScreenScaler(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);
        earlyRenderLoopScreen.init(minecraft, screenScaler.getScaledWidth(), screenScaler.getScaledHeight());
        earlyRenderLoopScreen.setTextRenderer(new TextRenderer(minecraft.options, "/font/default.png", minecraft.textureManager));
    }

    /**
     * @return true if using single-threaded EarlyRenderLoop mode
     */
    public static boolean isUsingEarlyRenderLoop() {
        return usingEarlyRenderLoop;
    }

    /**
     * Runs the early render loop (single-threaded mode).
     * Processes executor tasks while rendering until reload is complete.
     */
    public static void runEarlyRenderLoop() {
        if (!usingEarlyRenderLoop || earlyRenderLoopScreen == null) return;

        //noinspection deprecation
        val minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        val screenScaler = new ScreenScaler(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);
        val width = screenScaler.getScaledWidth();
        val height = screenScaler.getScaledHeight();
        val timer = ((MinecraftAccessor) minecraft).getTimer();
        val screen = earlyRenderLoopScreen;

        // Set up GL state
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, screenScaler.rawScaledWidth, screenScaler.rawScaledHeight, 0.0, 1000.0, 3000.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(0.0f, 0.0f, -2000.0f);
        glViewport(0, 0, minecraft.displayWidth, minecraft.displayHeight);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_FOG);

        try {
            LWJGLHelper.runEarlyRenderLoop(
                    () -> !earlyRenderLoopDone.get(),
                    () -> {
                        // Process ALL pending executor tasks each frame
                        Runnable task;
                        while ((task = ReloadScreenApplicationExecutor.INSTANCE.poll()) != null) {
                            task.run();
                        }

                        val f = timer.partialTick;
                        timer.advance();
                        timer.partialTick = f;
                        val mouseX = Mouse.getX() * width / minecraft.displayWidth;
                        val mouseY = height - Mouse.getY() * height / minecraft.displayHeight - 1;
                        screen.render(mouseX, mouseY, timer.partialTick);
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Clean up
        glDisable(GL_LIGHTING);
        glDisable(GL_FOG);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.1f);
        usingEarlyRenderLoop = false;
        earlyRenderLoopScreen = null;
        earlyRenderLoopDone = null;
    }

    /**
     * Threaded approach for LWJGL2 using SharedDrawable.
     */
    private static void onStartupThreaded(
            final Minecraft minecraft,
            final Object drawable
    ) {
        try {
            LWJGLHelper.makeCurrent(drawable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        val done = new AtomicBoolean();
        val localReloadScreen = new ReloadScreen(
                minecraft.currentScreen,
                () -> done.set(true),
                ReloadScreenTessellatorHolder.reloadScreenTessellator = TessellatorAccessor.stationapi_create(48)
        );
        val screenScaler = new ScreenScaler(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);
        val width = screenScaler.getScaledWidth();
        val height = screenScaler.getScaledHeight();
        localReloadScreen.init(minecraft, width, height);
        localReloadScreen.setTextRenderer(new TextRenderer(minecraft.options, "/font/default.png", minecraft.textureManager));
        val timer = ((MinecraftAccessor) minecraft).getTimer();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, screenScaler.rawScaledWidth, screenScaler.rawScaledHeight, 0.0, 1000.0, 3000.0);
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
            val f = timer.partialTick;
            timer.advance();
            timer.partialTick = f;
            val mouseX = Mouse.getX() * width / minecraft.displayWidth;
            val mouseY = height - Mouse.getY() * height / minecraft.displayHeight - 1;
            localReloadScreen.render(mouseX, mouseY, timer.partialTick);
            try {
                LWJGLHelper.displayUpdate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        glDisable(GL_LIGHTING);
        glDisable(GL_FOG);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.1f);
        try {
            LWJGLHelper.releaseContext(drawable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        thread = Optional.empty();
    }
}
