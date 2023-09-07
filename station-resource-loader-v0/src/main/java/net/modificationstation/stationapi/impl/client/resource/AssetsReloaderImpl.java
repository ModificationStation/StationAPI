package net.modificationstation.stationapi.impl.client.resource;

import com.mojang.datafixers.util.Function3;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.util.ScreenScaler;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsReloadEvent;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.tick.TickScheduler;
import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.resource.DefaultResourcePackProvider;
import net.modificationstation.stationapi.impl.resource.ResourcePackManager;
import net.modificationstation.stationapi.impl.resource.TexturePackProvider;
import net.modificationstation.stationapi.impl.resource.loader.ModResourcePackCreator;
import net.modificationstation.stationapi.mixin.resourceloader.client.MinecraftAccessor;
import net.modificationstation.stationapi.mixin.resourceloader.client.TessellatorAccessor;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static org.lwjgl.opengl.GL11.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class AssetsReloaderImpl {
    private static final ResourcePackManager RESOURCE_PACK_MANAGER = new ResourcePackManager(
            new DefaultResourcePackProvider(),
            ModResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER,
            new TexturePackProvider()
    );
    private static final CompletableFuture<Unit> COMPLETED_UNIT_FUTURE = CompletableFuture.completedFuture(Unit.INSTANCE);
    public static Thread reloadScreenThread;
    public static Drawable tmpDrawable;

    @EventListener
    private static void reload(TexturePackLoadedEvent.After event) {
        StationAPI.EVENT_BUS.post(AssetsReloadEvent.builder().build());
    }

    private static boolean firstLoad = true;

    @EventListener
    private static void reloadResourceManager(final AssetsReloadEvent event) throws LWJGLException {
        RESOURCE_PACK_MANAGER.scanPacks();
        //noinspection deprecation
        final Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        final Function3<Function<ScreenBase, Runnable>, Executor, Tessellator, AssetsReloadingScreen> reloadScreenFactory;
        reloadScreenFactory = (done, executor, tessellator) -> new AssetsReloadingScreen(
                minecraft.currentScreen,
                done.apply(minecraft.currentScreen),
                profilerFactory -> ReloadableAssetsManager.INSTANCE.reload(
                        Util.getMainWorkerExecutor(),
                        executor,
                        COMPLETED_UNIT_FUTURE,
                        profilerFactory,
                        RESOURCE_PACK_MANAGER.createResourcePacks()
                ),
                tessellator
        );
        if (firstLoad) {
            firstLoad = false;
            Drawable drawable = Display.getDrawable();
            drawable.releaseContext();
            tmpDrawable = new Pbuffer(1, 1, new PixelFormat(), drawable);
            tmpDrawable.makeCurrent();
            reloadScreenThread = new Thread(() -> onStartup(minecraft, reloadScreenFactory));
            reloadScreenThread.start();
            return;
        }
        TickScheduler.CLIENT_RENDER_START.immediate(
                () -> minecraft.openScreen(
                        reloadScreenFactory.apply(
                                parent -> () -> minecraft.openScreen(parent),
                                TickScheduler.CLIENT_RENDER_END::distributed,
                                Tessellator.INSTANCE
                        )
                )
        );
    }

    private static void onStartup(
            Minecraft minecraft,
            Function3<Function<ScreenBase, Runnable>, Executor, Tessellator, AssetsReloadingScreen> reloadScreenFactory
    ) {
        Drawable drawable = Display.getDrawable();
        try {
            drawable.makeCurrent();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
        final AtomicBoolean done = new AtomicBoolean();
        final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
        final AssetsReloadingScreen reloadScreen = reloadScreenFactory.apply(
                parent -> () -> done.set(true),
                tasks::add,
                TessellatorAccessor.stationapi_create(48)
        );
        final ScreenScaler screenScaler = new ScreenScaler(minecraft.options, minecraft.actualWidth, minecraft.actualHeight);
        final int
                width = screenScaler.getScaledWidth(),
                height = screenScaler.getScaledHeight();
        minecraft.textRenderer = new TextRenderer(minecraft.options, "/font/default.png", minecraft.textureManager);
        reloadScreen.init(minecraft, width, height);
        final Timer timer = ((MinecraftAccessor) minecraft).getTickTimer();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, screenScaler.scaledWidth, screenScaler.scaledHeight, 0.0, 1000.0, 3000.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(0.0f, 0.0f, -2000.0f);
        glViewport(0, 0, minecraft.actualWidth, minecraft.actualHeight);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_FOG);
        while (!done.get()) {
            while (true) if (!Mouse.next()) break;
            while (true) if (!Keyboard.next()) break;
            final float f = timer.field_2370;
            timer.method_1853();
            timer.field_2370 = f;
            final int
                    mouseX = Mouse.getX() * width / minecraft.actualWidth,
                    mouseY = height - Mouse.getY() * height / minecraft.actualHeight - 1;
            reloadScreen.render(mouseX, mouseY, timer.field_2370);
            Display.update();
            { // application
                Runnable command = tasks.poll();
                if (command != null) command.run();
            }
        }
        glDisable(GL_LIGHTING);
        glDisable(GL_FOG);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.1f);
        try {
            drawable.releaseContext();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
    }
}
