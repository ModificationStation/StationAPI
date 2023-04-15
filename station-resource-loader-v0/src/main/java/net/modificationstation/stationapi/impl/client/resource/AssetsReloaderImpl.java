package net.modificationstation.stationapi.impl.client.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraft.client.gui.screen.ScreenBase;
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
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class AssetsReloaderImpl {

    private static final ResourcePackManager RESOURCE_PACK_MANAGER = new ResourcePackManager(
            new DefaultResourcePackProvider(),
            ModResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER,
            new TexturePackProvider()
    );
    private static final CompletableFuture<Unit> COMPLETED_UNIT_FUTURE = CompletableFuture.completedFuture(Unit.INSTANCE);

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void reload(TexturePackLoadedEvent.After event) {
        StationAPI.EVENT_BUS.post(AssetsReloadEvent.builder().build());
    }

    private static boolean firstLoad = true;

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void reloadResourceManager(final AssetsReloadEvent event) throws LWJGLException {
        RESOURCE_PACK_MANAGER.scanPacks();
        //noinspection deprecation
        final Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        final BiFunction<Function<ScreenBase, Runnable>, Executor, AssetsReloadingScreen> reloadScreenFactory;
        reloadScreenFactory = (done, executor) -> new AssetsReloadingScreen(
                minecraft.currentScreen,
                done.apply(minecraft.currentScreen),
                profilerFactory -> ReloadableAssetsManager.INSTANCE.reload(
                        Util.getMainWorkerExecutor(),
                        executor,
                        COMPLETED_UNIT_FUTURE,
                        profilerFactory,
                        RESOURCE_PACK_MANAGER.createResourcePacks()
                )
        );
        if (firstLoad) {
            firstLoad = false;
            onStartup(minecraft, reloadScreenFactory);
            return;
        }
        TickScheduler.CLIENT_RENDER_START.immediate(
                () -> minecraft.openScreen(
                        reloadScreenFactory.apply(
                                parent -> () -> minecraft.openScreen(parent),
                                TickScheduler.CLIENT_RENDER_END::distributed
                        )
                )
        );
    }

    private static void onStartup(
            Minecraft minecraft,
            BiFunction<Function<ScreenBase, Runnable>, Executor, AssetsReloadingScreen> reloadScreenFactory
    ) throws LWJGLException {
        final AtomicBoolean done = new AtomicBoolean();
        final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
        final AssetsReloadingScreen reloadScreen = reloadScreenFactory.apply(
                parent -> () -> done.set(true),
                tasks::add
        );
        final ScreenScaler screenScaler = new ScreenScaler(minecraft.options, minecraft.actualWidth, minecraft.actualHeight);
        final int
                width = screenScaler.getScaledWidth(),
                height = screenScaler.getScaledHeight();
        final boolean noTextRenderer = minecraft.textRenderer == null;
        if (noTextRenderer)
            minecraft.textRenderer = new TextRenderer(minecraft.options, "/font/default.png", minecraft.textureManager);
        reloadScreen.init(minecraft, width, height);
        if (noTextRenderer)
            minecraft.textRenderer = null;
        final Timer timer = ((MinecraftAccessor) minecraft).getTickTimer();
        GL11.glClear(16640);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, screenScaler.scaledWidth, screenScaler.scaledHeight, 0.0, 1000.0, 3000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
        GL11.glViewport(0, 0, minecraft.actualWidth, minecraft.actualHeight);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glDisable(2896);
        GL11.glEnable(3553);
        GL11.glDisable(2912);
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
            Display.swapBuffers();
            { // application
                Runnable command = tasks.poll();
                if (command != null) command.run();
            }
        }
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1f);
    }

}
