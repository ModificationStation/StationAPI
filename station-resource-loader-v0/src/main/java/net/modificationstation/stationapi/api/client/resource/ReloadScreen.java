package net.modificationstation.stationapi.api.client.resource;

import com.google.common.primitives.Floats;
import com.google.common.primitives.Longs;
import cyclops.control.Option;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import lombok.val;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.resource.ResourceReload;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletionException;
import java.util.function.*;

import static org.lwjgl.opengl.GL11.*;

class ReloadScreen extends ScreenBase {
    private static final long
            MAX_FPS = 60,
            BACKGROUND_START = 0,
            BACKGROUND_FADE_IN = 1000,
            STAGE_0_START = BACKGROUND_START + BACKGROUND_FADE_IN,
            STAGE_0_FADE_IN = 2000,
            GLOBAL_FADE_OUT = 1000,
            RELOAD_START = STAGE_0_START + STAGE_0_FADE_IN,
            EXCEPTION_FADE_IN = 500;
    private static final Double2DoubleFunction
            SIN_90_DELTA = delta -> MathHelper.sin((float) (delta * Math.PI / 2));
    private static final Long2DoubleFunction
            BACKGROUND_FADE_IN_DELTA = time -> (double) Longs.constrainToRange(time - BACKGROUND_START, 0, BACKGROUND_FADE_IN) / BACKGROUND_FADE_IN,
            STAGE_0_FADE_IN_DELTA = time -> (double) Longs.constrainToRange(time - STAGE_0_START, 0, STAGE_0_FADE_IN) / STAGE_0_FADE_IN;
    private static final Function<LongSupplier, Long2DoubleFunction>
            GLOBAL_FADE_OUT_DELTA_FACTORY = fadeOutStartGetter -> time -> 1 - (double) Longs.constrainToRange(time - fadeOutStartGetter.getAsLong(), 0, GLOBAL_FADE_OUT) / GLOBAL_FADE_OUT;
    private static final Function<LongSupplier, Long2DoubleFunction>
            BACKGROUND_EXCEPTION_FADE_IN_FACTORY = fadeInStartGetter -> time -> 1;

    private static UnaryOperator<Long2DoubleFunction> when(BooleanSupplier condition, Long2DoubleFunction ifTrue) {
        return ifFalse -> value -> (condition.getAsBoolean() ? ifTrue : ifFalse).applyAsDouble(value);
    }

    private final ScreenBase parent;
    private final Runnable done;
    private final Tessellator tessellator;
    private boolean firstRenderTick = true;
    private long initTimestamp;
    private long currentTime;
    private float progress;
    private final Runnable
            backgroundEmitter,
            stage0Emitter;
    private boolean finished;
    private long fadeOutStart;
    private float scrollProgress;
    private final String logo;
    private boolean exceptionThrown;
    private long exceptionStart;

    ReloadScreen(
            ScreenBase parent,
            Runnable done,
            Tessellator tessellator
    ) {
        ReloadScreenManager.reloadScreen = this;
        this.parent = parent;
        this.done = done;
        this.tessellator = tessellator;

        UnaryOperator<Long2DoubleFunction> globalFadeOutComposer = when(() -> finished, GLOBAL_FADE_OUT_DELTA_FACTORY.apply(() -> fadeOutStart));
        Long2DoubleFunction
                backgroundDelta = globalFadeOutComposer.apply(BACKGROUND_FADE_IN_DELTA),
                stage0Delta = globalFadeOutComposer.apply(STAGE_0_FADE_IN_DELTA);
        backgroundEmitter = setupEmitter(backgroundDelta.andThenDouble(SIN_90_DELTA), this::renderBackground);
        stage0Emitter = setupEmitter(stage0Delta.andThenDouble(SIN_90_DELTA), this::renderProgressBar, this::renderLogo);
        logo = "/assets/station-resource-loader-v0/textures/gui/stationapi_reload" + switch (new Random().nextInt(100)) {
            case 0 -> "_dimando";
            case 1 -> "_old";
            default -> "";
        } + ".png";
   }

    private Runnable setupEmitter(final Long2DoubleFunction deltaFunc, final DoubleConsumer... renderers) {
        DoubleConsumer renderer = Arrays.stream(renderers).reduce(DoubleConsumer::andThen).orElse(delta -> {});
        return () -> {
            double delta = deltaFunc.applyAsDouble(currentTime);
            if (delta > 0)
                renderer.accept(delta);
        };
    }

    private void renderBackground(double delta) {
        fill(0, 0, width, height,
                this.parent == null ?
                        0xFF << 24 |
                                (int) (finished ? 0xFF - delta * (0xFF - 0x35) : delta * 0x35) << 16 |
                                (int) (finished ? 0xFF - delta * (0xFF - 0x86) : delta * 0x86) << 8 |
                                (int) (finished ? 0xFF - delta * (0xFF - 0xE7) : delta * 0xE7) :
                        (int) (delta * 0xFF) << 24 | 0x3586E7
        );
    }

    private void renderProgressBar(double delta) {
        int color = (int) (delta * 0xFF) << 24 | 0xFFFFFF;
        float v = (float) (10 - delta * 10);
        drawLineHorizontal(40, width - 40 - 1, (int) (height - 90 + v * 5), color);
        drawLineHorizontal(40, width - 40 - 1, (int) (height - 50 + v), color);
        drawLineHorizontal(40, width - 40 - 1, (int) (height - 40 - 1 + v), color);
        drawLineVertical(40, (int) (height - 40 + v), (int) (height - 90 + v * 5), color);
        drawLineVertical(width - 40 - 1, (int) (height - 40 + v), (int) (height - 90 + v * 5), color);
        fill(40 + 3, (int) (height - 50 + 3 + v), net.modificationstation.stationapi.api.util.math.MathHelper.ceil((width - (40 + 3) * 2) * progress + 40 + 3), (int) (height - 40 - 3 + v), color);
        float
                xScale = (float) minecraft.actualWidth / width,
                yScale = (float) minecraft.actualHeight / height;
        int scissorsHeight = (int) ((40 - 1 - v * 4) * yScale);
        if (scissorsHeight > 0) {
            int to = MathHelper.floor(scrollProgress);
            float scrollDelta = scrollProgress - to;
            glEnable(GL_SCISSOR_TEST);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glScissor((int) ((40 + 3) * xScale), (int) ((50 - v) * yScale), (int) ((width - (40 + 3) * 2) * xScale), scissorsHeight);
            for (int i = 0; i < to + 1; i++) {
                int y = net.modificationstation.stationapi.api.util.math.MathHelper.ceil(height - 98 + (10 * i) + (scrollDelta * 10) + v * 5);
                if (y > height - 50 + v) break;
                drawTextWithShadow(textManager, ReloadScreenManager.LOCATIONS.get(to - i), 40 + 3, y, color);
            }
            glDisable(GL_BLEND);
            glDisable(GL_SCISSOR_TEST);
        }
    }

    private void renderLogo(double delta) {
        double v = 10 - delta * 10;
        minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId(logo));
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        tessellator.start();
        tessellator.colour(0xFF, 0xFF, 0xFF, (int) (0xFF * delta));
        tessellator.vertex(width / 2D - 120, (height - 90D) / 2 - 20 - v, 0, 0, 0);
        tessellator.vertex(width / 2D - 120, (height - 90D) / 2 + 20 - v, 0, 0, 1);
        tessellator.vertex(width / 2D + 120, (height - 90D) / 2 + 20 - v, 0, 1, 1);
        tessellator.vertex(width / 2D + 120, (height - 90D) / 2 - 20 - v, 0, 1, 0);
        tessellator.draw();
        glDisable(GL_BLEND);
    }

    @Override
    public void renderBackground() {
        backgroundEmitter.run();
    }

    private long lastRender;

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        if (firstRenderTick) {
            firstRenderTick = false;
            initTimestamp = System.currentTimeMillis();
        }
        currentTime = System.currentTimeMillis() - initTimestamp;
        val partial = currentTime - lastRender < (1000 / MAX_FPS);
        if (partial) currentTime = lastRender;
        else lastRender = currentTime;
        val locationsSize = ReloadScreenManager.LOCATIONS.size();
        if (!finished && !(scrollProgress + .1 < locationsSize) && !(progress + .1 < 1) && ReloadScreenManager.isReloadComplete()) {
            try {
                ReloadScreenManager.getCurrentReload().peek(ResourceReload::throwException);
                finished = true;
                fadeOutStart = currentTime;
            } catch (CompletionException e) {
                exceptionThrown = true;
                exceptionStart = currentTime;
            }
        }
        if (!partial) {
            Option<ResourceReload> reload;
            progress = Floats.constrainToRange(progress * .95F + (isReloadStarted() && (reload = ReloadScreenManager.getCurrentReload()).isPresent() ? reload.orElse(null/*safe*/).getProgress() : 0) * .05F, 0, 1);
            scrollProgress = Floats.constrainToRange(scrollProgress * .95F + locationsSize * .05F, 0, locationsSize);
        }
        if ((finished ? currentTime <= fadeOutStart + GLOBAL_FADE_OUT : currentTime < BACKGROUND_START + BACKGROUND_FADE_IN) && parent != null)
            parent.render(mouseX, mouseY, delta);
        renderBackground();
        super.render(mouseX, mouseY, delta);
        stage0Emitter.run();
        if (finished && currentTime - fadeOutStart > GLOBAL_FADE_OUT) {
            ReloadScreenManager.onFinish();
            done.run();
        }
    }

    boolean isReloadStarted() {
        return currentTime > RELOAD_START;
    }

    @Override
    protected void drawLineHorizontal(int startX, int endX, int y, int color) {
        if (endX < startX) {
            val n = startX;
            startX = endX;
            endX = n;
        }
        fill(startX, y, endX + 1, y + 1, color);
    }

    @Override
    protected void drawLineVertical(int x, int startY, int endY, int color) {
        if (endY < startY) {
            val n = startY;
            startY = endY;
            endY = n;
        }
        fill(x, startY + 1, x + 1, endY, color);
    }

    @Override
    protected void fill(int startX, int startY, int endX, int endY, int color) {
        int n;
        if (startX < endX) {
            n = startX;
            startX = endX;
            endX = n;
        }
        if (startY < endY) {
            n = startY;
            startY = endY;
            endY = n;
        }
        val a = (float)(color >> 24 & 0xFF) / 255.0f;
        val r = (float)(color >> 16 & 0xFF) / 255.0f;
        val g = (float)(color >> 8 & 0xFF) / 255.0f;
        val b = (float)(color & 0xFF) / 255.0f;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(r, g, b, a);
        tessellator.start();
        tessellator.addVertex(startX, endY, 0.0);
        tessellator.addVertex(endX, endY, 0.0);
        tessellator.addVertex(endX, startY, 0.0);
        tessellator.addVertex(startX, startY, 0.0);
        tessellator.draw();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    void setTextRenderer(TextRenderer textRenderer) {
        textManager = textRenderer;
    }
}
