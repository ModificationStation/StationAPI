package net.modificationstation.stationapi.api.client.resource;

import com.google.common.primitives.Floats;
import com.google.common.primitives.Longs;
import cyclops.control.Option;
import cyclops.function.Effect;
import cyclops.function.FluentFunctions;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import lombok.val;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.resource.ResourceReload;
import net.modificationstation.stationapi.api.util.math.ColorHelper;
import net.modificationstation.stationapi.config.LoadingScreenOption;
import net.modificationstation.stationapi.config.StationConfig;
import net.modificationstation.stationapi.impl.client.resource.ReloadScreenManagerImpl;

import java.util.Random;
import java.util.concurrent.CompletionException;
import java.util.function.*;

import static cyclops.function.FluentFunctions.expression;
import static java.util.Map.of;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.util.math.MathHelper.ceil;
import static net.modificationstation.stationapi.api.util.math.MathHelper.lerp;
import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("UnstableApiUsage")
class ReloadScreen extends Screen {
    private static final long MAX_FPS = 60;
    private static final int BACKGROUND_COLOR_DEFAULT_RED = 0x35;
    private static final int BACKGROUND_COLOR_DEFAULT_GREEN = 0x86;
    private static final int BACKGROUND_COLOR_DEFAULT_BLUE = 0xE7;
    private static final int BACKGROUND_COLOR_EXCEPTION_RED = 0xFF;
    private static final int BACKGROUND_COLOR_EXCEPTION_GREEN = 0x29;
    private static final int BACKGROUND_COLOR_EXCEPTION_BLUE = 0x29;

    private static final Object BACKGROUND_DEFAULT_DELTA_KEY = new Object();
    private static final Object BACKGROUND_EXCEPTION_DELTA_KEY = new Object();
    private static final Object STAGE_0_DEFAULT_DELTA_KEY = new Object();
    private static final Object STAGE_0_EXCEPTION_DELTA_KEY = new Object();

    private static final Double2DoubleFunction SIN_90_DELTA = delta -> MathHelper.sin((float) (delta * Math.PI / 2));
    private static final Double2DoubleFunction COS_90_DELTA = delta -> MathHelper.cos((float) (delta * Math.PI / 2));
    private static final Double2DoubleFunction INVERSE_DELTA = delta -> 1 - delta;

    private static final String LOGO_TEMPLATE = "/assets/station-resource-loader-v0/textures/gui/stationapi_reload%s.png";
    private static final String LOGO_MOJANG = "/title/mojang.png";

    private final long BACKGROUND_START = 0;
    private long BACKGROUND_FADE_IN = 1000;
    private long STAGE_0_START = BACKGROUND_START + BACKGROUND_FADE_IN;
    private long STAGE_0_FADE_IN = 2000;
    private long GLOBAL_FADE_OUT = 1000;
    private long RELOAD_START = STAGE_0_START + STAGE_0_FADE_IN;
    private long EXCEPTION_TRANSFORM = 500;

    private final Screen parent;
    private final Runnable done;
    private final Tessellator tessellator;
    private boolean firstRenderTick = true;
    private long initTimestamp;
    private long currentTime;
    private float progress;
    private final Effect backgroundEmitter;
    private final Effect stage0Emitter;
    private boolean finished;
    private long fadeOutStart;
    private float scrollProgress;
    private final String logo;
    private boolean exceptionThrown;
    private long exceptionStart;
    private Exception exception;
    private final String stapiLogo;

    ReloadScreen(
            Screen parent,
            Runnable done,
            Tessellator tessellator
    ) {
        ReloadScreenManager.reloadScreen = this;
        this.parent = parent;
        this.done = done;
        this.tessellator = tessellator;

        stapiLogo = LOGO_TEMPLATE.formatted(
                switch (new Random().nextInt(100)) {
                    case 0 -> "_dimando";
                    case 1 -> "_old";
                    default -> "";
                }
        );

        logo = stapiLogo;

        if(
                isScreenType(LoadingScreenOption.NO_ANIMATE) ||
                isScreenType(LoadingScreenOption.HIDE) ||
                isScreenType(LoadingScreenOption.FORGE)
        ) {
            BACKGROUND_FADE_IN =
            STAGE_0_START =
            STAGE_0_FADE_IN =
            GLOBAL_FADE_OUT =
            RELOAD_START =
            EXCEPTION_TRANSFORM = 1;
        }
        else {
            BACKGROUND_FADE_IN = 1000;
            STAGE_0_START = BACKGROUND_START + BACKGROUND_FADE_IN;
            STAGE_0_FADE_IN = 2000;
            GLOBAL_FADE_OUT = 1000;
            RELOAD_START = STAGE_0_START + STAGE_0_FADE_IN;
            EXCEPTION_TRANSFORM = 500;
        }
        Long2DoubleFunction backgroundFadeInDelta = time -> (double) Longs.constrainToRange(time - BACKGROUND_START, 0, BACKGROUND_FADE_IN) / BACKGROUND_FADE_IN;
        Long2DoubleFunction stage0FadeInDelta = time -> (double) Longs.constrainToRange(time - STAGE_0_START, 0, STAGE_0_FADE_IN) / STAGE_0_FADE_IN;

        Function<LongSupplier, Long2DoubleFunction> globalFadeOutDeltaFactory = fadeOutStartGetter -> INVERSE_DELTA.composeLong(time -> (double) Longs.constrainToRange(time - fadeOutStartGetter.getAsLong(), 0, GLOBAL_FADE_OUT) / GLOBAL_FADE_OUT);
        Function<LongSupplier, Long2DoubleFunction> backgroundExceptionFadeInFactory = fadeInStartGetter -> time -> (double) Longs.constrainToRange(time - fadeInStartGetter.getAsLong(), 0, EXCEPTION_TRANSFORM) / EXCEPTION_TRANSFORM;
        Function<LongSupplier, Long2DoubleFunction> stage0ExceptionTransformFactory = transformStartGetter -> time -> (double) Longs.constrainToRange(time - transformStartGetter.getAsLong(), 0, EXCEPTION_TRANSFORM) / EXCEPTION_TRANSFORM;

        val globalFadeOutComposer = when(() -> finished, globalFadeOutDeltaFactory.apply(() -> fadeOutStart));
        val deltaMap = of(
                BACKGROUND_DEFAULT_DELTA_KEY, globalFadeOutComposer.apply(backgroundFadeInDelta).andThenDouble(SIN_90_DELTA),
                BACKGROUND_EXCEPTION_DELTA_KEY, backgroundExceptionFadeInFactory.apply(() -> exceptionStart).andThenDouble(COS_90_DELTA).andThenDouble(INVERSE_DELTA),
                STAGE_0_DEFAULT_DELTA_KEY, globalFadeOutComposer.apply(stage0FadeInDelta).andThenDouble(SIN_90_DELTA),
                STAGE_0_EXCEPTION_DELTA_KEY, stage0ExceptionTransformFactory.apply(() -> exceptionStart).andThenDouble(COS_90_DELTA)
        );

        ToDoubleFunction<Object> deltaFunc = key -> deltaMap.get(key).applyAsDouble(currentTime);

        backgroundEmitter = FluentFunctions
                .<ToDoubleFunction<Object>>expression(this::renderBackground)
                .partiallyApply(deltaFunc)::get;

        stage0Emitter =
                expression(this::renderProgressBar)
                .before(this::renderLogo)
                .partiallyApply(deltaFunc)::get;
    }

    private void renderBackground(ToDoubleFunction<Object> deltaFunc) {
        val delta = deltaFunc.applyAsDouble(BACKGROUND_DEFAULT_DELTA_KEY);
        int red = BACKGROUND_COLOR_DEFAULT_RED;
        int green = BACKGROUND_COLOR_DEFAULT_GREEN;
        int blue = BACKGROUND_COLOR_DEFAULT_BLUE;
        if (
                isScreenType(LoadingScreenOption.HIDE) ||
                isScreenType(LoadingScreenOption.FORGE) ||
                isScreenType(LoadingScreenOption.NO_RECOLOR)
        ) {
            red = green = blue = 0xFF;
        }
        final int color;
        if (exceptionThrown) {
            val exceptionDelta = deltaFunc.applyAsDouble(BACKGROUND_EXCEPTION_DELTA_KEY);
            color = ColorHelper.Argb.getArgb(
                    0xFF,
                    lerp(exceptionDelta, red, BACKGROUND_COLOR_EXCEPTION_RED),
                    lerp(exceptionDelta, green, BACKGROUND_COLOR_EXCEPTION_GREEN),
                    lerp(exceptionDelta, blue, BACKGROUND_COLOR_EXCEPTION_BLUE)
            );
        } else color = parent == null ?
                finished ?
                        ColorHelper.Argb.getArgb(
                                0xFF,
                                lerp(delta, 0xFF, red),
                                lerp(delta, 0xFF, green),
                                lerp(delta, 0xFF, blue)
                        ) :
                        ColorHelper.Argb.getArgb(
                                0xFF,
                                (int) (delta * red),
                                (int) (delta * green),
                                (int) (delta * blue)
                        ) :
                ColorHelper.Argb.getArgb(
                        (int) (delta * 0xFF),
                        red,
                        green,
                        blue
                );
        fill(0, 0, width, height, color);
    }

    private void renderProgressBar(ToDoubleFunction<Object> deltaFunc) {
        if (isScreenType(LoadingScreenOption.HIDE)) return;
        if (isScreenType(LoadingScreenOption.FORGE)) {
            if(ReloadScreenManager.LOCATIONS.isEmpty()) return;
            String text = ReloadScreenManager.LOCATIONS.get(ReloadScreenManager.LOCATIONS.size() - 1);
            fill(3, height, textRenderer.getWidth(text) + 7, height - 12, -1073741824);
            glEnable(GL_BLEND);
            drawTextWithShadow(textRenderer, text, 5, height - 10, 0xFFFFFF);
            glDisable(GL_BLEND);
            return;
        }
        val delta = deltaFunc.applyAsDouble(STAGE_0_DEFAULT_DELTA_KEY);
        if (delta == 0) return;
        val color = (int) (delta * 0xFF) << 24 | 0xFFFFFF;
        val v = (float) (10 - delta * 10);
        val ev = exceptionThrown ? (float) (10 - deltaFunc.applyAsDouble(STAGE_0_EXCEPTION_DELTA_KEY) * 10) * 3 - 1 : 0;
        if (isScreenType(LoadingScreenOption.NO_RECOLOR))
            fill(38, height - 37 + (int) v, width - 38, (int) (height - 105 + v * 5), ColorHelper.Argb.getArgb(lerp(delta, 0, 192), 0, 0, 0));
        drawHorizontalLine(40, width - 40 - 1, (int) (height - 90 + v * 5), color);
        if (exceptionThrown) drawHorizontalLine(40, width - 40 - 1, (int) (height - 90 + v * 5 + ev), color);
        drawHorizontalLine(40, width - 40 - 1, (int) (height - 50 + v), color);
        drawHorizontalLine(40, width - 40 - 1, (int) (height - 40 - 1 + v), color);
        drawVerticalLine(40, (int) (height - 40 + v), (int) (height - 90 + v * 5), color);
        drawVerticalLine(width - 40 - 1, (int) (height - 40 + v), (int) (height - 90 + v * 5), color);
        fill(40 + 3, (int) (height - 50 + 3 + v), ceil((width - (40 + 3) * 2) * progress + 40 + 3), (int) (height - 40 - 3 + v), color);
        val xScale = (float) minecraft.displayWidth / width;
        val yScale = (float) minecraft.displayHeight / height;
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        val locationsScissorsHeight = (int) ((40 - 1 - v * 4 + ev) * yScale);
        if (locationsScissorsHeight > 0) {
            val to = ceil(scrollProgress);
            val scrollDelta = scrollProgress - to;
            glEnable(GL_SCISSOR_TEST);
            glScissor((int) ((40 + 3) * xScale), (int) ((50 - v) * yScale), (int) ((width - (40 + 3) * 2) * xScale), locationsScissorsHeight);
            for (int i = 0; i < to; i++) {
                val y = ceil(height - 88 + (10 * i) + (scrollDelta * 10) + v * 5 + ev);
                if (y > height - 50 + v) break;
                drawTextWithShadow(textRenderer, ReloadScreenManager.LOCATIONS.get(to - i - 1), 40 + 3, y, color);
            }
            glDisable(GL_SCISSOR_TEST);
        }
        val exceptionScissorsHeight = (int) ((-1 - v * 4 + ev) * yScale);
        if (exceptionThrown && exceptionScissorsHeight > 0) {
            glEnable(GL_SCISSOR_TEST);
            glScissor((int) ((40 + 3) * xScale), (int) ((91 - v - ev) * yScale), (int) ((width - (40 + 3) * 2) * xScale), exceptionScissorsHeight);
            val line = exception.getMessage();
            var curHeight = height - 88;
            val lineWidth = textRenderer.getWidth(line);
            if (lineWidth > width - (40 + 3) * 2) {
                var begin = 0;
                var lastSpace = -1;
                for (int cur = 0, lineLength = line.length(); cur < lineLength; cur++) {
                    val isSpace = line.charAt(cur) == ' ';
                    val isEnd = cur + 1 == lineLength;
                    if (isSpace || isEnd) {
                        val newLine = isEnd ? line.substring(begin) : line.substring(begin, cur);
                        val newLineWidth = textRenderer.getWidth(newLine);
                        if (newLineWidth > width - (40 + 3) * 2) {
                            drawTextWithShadow(textRenderer, line.substring(begin, lastSpace), 40 + 3, curHeight, color);
                            curHeight += 10;
                            begin = lastSpace + 1;
                        }
                        if (isSpace)
                            lastSpace = cur;
                        if (isEnd) {
                            drawTextWithShadow(textRenderer, line.substring(begin), 40 + 3, curHeight, color);
                            curHeight += 10;
                        }
                    }
                }
            } else drawTextWithShadow(textRenderer, line, 40 + 3, curHeight, color);
            glDisable(GL_SCISSOR_TEST);
        }
        drawTextWithShadow(textRenderer, "Minecraft: " + (
                ReloadScreenManagerImpl.isMinecraftDone ?
                        "Done" :
                        "Working..."
        ), 40 + 3, (int) (height - 100 + v * 5), color);
        val stationStatus = "StationAPI: " + (
                isReloadStarted() ?
                        ReloadScreenManager.isReloadComplete() ?
                                "Done" :
                                "Working..." :
                        "Idle"
        );
        drawTextWithShadow(textRenderer, stationStatus, width - 40 - 3 - textRenderer.getWidth(stationStatus), (int) (height - 100 + v * 5), color);
        glDisable(GL_BLEND);
    }

    private void renderLogo(ToDoubleFunction<Object> deltaFunc) {
        if (
                isScreenType(LoadingScreenOption.SHOW) ||
                isScreenType(LoadingScreenOption.NO_ANIMATE) ||
                isScreenType(LoadingScreenOption.NO_RECOLOR)
        ) {
            int color = 0xFFFFFFFF;
            if (isScreenType(LoadingScreenOption.NO_RECOLOR)) {
                color = 0xFFDD4F3B; // mojang colour
            }
            renderStapiLogo(deltaFunc, (width / 2f), (height / 2f - 20), 120, 20, color);
        }

        if (
                isScreenType(LoadingScreenOption.HIDE) ||
                isScreenType(LoadingScreenOption.FORGE)
        ) {
            renderMojangLogo(deltaFunc, width / 2f, height / 2f, width, height, 0, 0); // Draws the top left pixel of the logo, which is white, normally.
            renderMojangLogo(deltaFunc, (width / 2f) - 1, (height / 2f) + 1, 128, 128, 1, 1);
        }

        if (isScreenType(LoadingScreenOption.FORGE)) {
            int color;
            if (exceptionThrown) {
                val exceptionDelta = deltaFunc.applyAsDouble(BACKGROUND_EXCEPTION_DELTA_KEY);
                color = ColorHelper.Argb.getArgb(
                        0xFF,
                        lerp(exceptionDelta, BACKGROUND_COLOR_DEFAULT_RED, BACKGROUND_COLOR_EXCEPTION_RED),
                        lerp(exceptionDelta, BACKGROUND_COLOR_DEFAULT_GREEN, BACKGROUND_COLOR_EXCEPTION_GREEN),
                        lerp(exceptionDelta, BACKGROUND_COLOR_DEFAULT_BLUE, BACKGROUND_COLOR_EXCEPTION_BLUE)
                );
            }
            else {
                color = ColorHelper.Argb.getArgb(0xFF, BACKGROUND_COLOR_DEFAULT_RED, BACKGROUND_COLOR_DEFAULT_GREEN, BACKGROUND_COLOR_DEFAULT_BLUE);
            }
            // Stapi logo is 6:1 aspect
            renderStapiLogo(deltaFunc, width - 50, height - 14, 48, 8, color);
        }
    }

    private void renderMojangLogo(ToDoubleFunction<Object> deltaFunc, float x, float y, float w, float h, float u, float v) {
        val delta = deltaFunc.applyAsDouble(STAGE_0_DEFAULT_DELTA_KEY);
        if (delta == 0) return;
        minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId(LOGO_MOJANG));
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        tessellator.startQuads();
        tessellator.color(0xFF, 0xFF, 0xFF, 0xFF);
        tessellator.vertex(x - w, y - h - v, 0, 0, 0);
        tessellator.vertex(x - w, y + h - v, 0, 0, v);
        tessellator.vertex(x + w, y + h - v, 0, u, v);
        tessellator.vertex(x + w, y - h - v, 0, u, 0);
        tessellator.draw();
        glDisable(GL_BLEND);
    }

    private void renderStapiLogo(ToDoubleFunction<Object> deltaFunc, float x, float y, float w, float h, int color) {
        double delta = deltaFunc.applyAsDouble(STAGE_0_DEFAULT_DELTA_KEY);
        if (delta == 0) return;
        double v = 10 - delta * 10;
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId(logo));
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        tessellator.startQuads();
        tessellator.color(r, g, b, (int) (a * delta));
        tessellator.vertex(x - w, y - h - v, 0, 0, 0);
        tessellator.vertex(x - w, y + h - v, 0, 0, 1);
        tessellator.vertex(x + w, y + h - v, 0, 1, 1);
        tessellator.vertex(x + w, y - h - v, 0, 1, 0);
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
        if (!exceptionThrown && !finished && !(scrollProgress + .1 < locationsSize) && !(progress + .1 < 1) && ReloadScreenManager.isReloadComplete()) {
            try {
                ReloadScreenManager.getCurrentReload().peek(ResourceReload::throwException);
                finished = true;
                fadeOutStart = currentTime;
            } catch (CompletionException e) {
                exceptionThrown = true;
                exceptionStart = currentTime;
                exception = e;
                LOGGER.error("An exception occurred during resource loading", e);
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
    protected void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            val n = startX;
            startX = endX;
            endX = n;
        }
        fill(startX, y, endX + 1, y + 1, color);
    }

    @Override
    protected void drawVerticalLine(int x, int startY, int endY, int color) {
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
        tessellator.startQuads();
        tessellator.vertex(startX, endY, 0.0);
        tessellator.vertex(endX, endY, 0.0);
        tessellator.vertex(endX, startY, 0.0);
        tessellator.vertex(startX, startY, 0.0);
        tessellator.draw();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    void setTextRenderer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    boolean isScreenType(LoadingScreenOption loadingScreenOption) {
        return StationConfig.stationConfigData.loadingScreenOption.equals(loadingScreenOption);
    }

    private UnaryOperator<Long2DoubleFunction> when(BooleanSupplier condition, Long2DoubleFunction ifTrue) {
        return ifFalse -> value -> (condition.getAsBoolean() ? ifTrue : ifFalse).applyAsDouble(value);
    }
}
