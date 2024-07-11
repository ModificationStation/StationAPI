package net.modificationstation.stationapi.api.client.resource;

import com.google.common.primitives.Floats;
import cyclops.control.Option;
import it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.resource.ResourceReload;
import net.modificationstation.stationapi.api.util.math.ColorHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionException;
import java.util.function.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.util.math.MathHelper.ceil;
import static net.modificationstation.stationapi.api.util.math.MathHelper.lerp;
import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("UnstableApiUsage")
class ReloadScreen extends Screen {
    private static final int BACKGROUND_COLOR_DEFAULT_RED = 0x35;
    private static final int BACKGROUND_COLOR_DEFAULT_GREEN = 0x86;
    private static final int BACKGROUND_COLOR_DEFAULT_BLUE = 0xE7;
    private static final int BACKGROUND_COLOR_EXCEPTION_RED = 0xFF;
    private static final int BACKGROUND_COLOR_EXCEPTION_GREEN = 0x29;
    private static final int BACKGROUND_COLOR_EXCEPTION_BLUE = 0x29;

    private static final Object BACKGROUND_DEFAULT_DELTA_KEY = new Object();
    private static final Object BACKGROUND_EXCEPTION_DELTA_KEY = new Object();

    private static final String
            LOGO_TEMPLATE = "/assets/station-resource-loader-v0/textures/gui/stationapi_reload%s.png";

    private static UnaryOperator<Long2DoubleFunction> when(BooleanSupplier condition, Long2DoubleFunction ifTrue) {
        return ifFalse -> value -> (condition.getAsBoolean() ? ifTrue : ifFalse).applyAsDouble(value);
    }

    private final Screen parent;
    private final Runnable done;
    private final Tessellator tessellator;
    private float progress;
    private boolean finished;
    private final String logo;
    private boolean exceptionThrown;
    private Exception exception;

    ReloadScreen(
            Screen parent,
            Runnable done,
            Tessellator tessellator
    ) {
        ReloadScreenManager.reloadScreen = this;
        this.parent = parent;
        this.done = done;
        this.tessellator = tessellator;

        logo = LOGO_TEMPLATE.formatted(
                switch (new Random().nextInt(100)) {
                    case 0 -> "_dimando";
                    case 1 -> "_old";
                    default -> "";
                }
        );
    }


    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    static {
        NUMBER_FORMAT.setMinimumFractionDigits(2);
        NUMBER_FORMAT.setMaximumFractionDigits(2);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        if (parent == null) renderEarly();
        else renderNormal(delta);

        Option<ResourceReload> reload;
        progress = Floats.constrainToRange(progress * .95F + ((reload = ReloadScreenManager.getCurrentReload()).isPresent() ? reload.orElse(null).getProgress() : 0) * .05F, 0, 1);
        if (Float.isNaN(progress)) progress = 0;
        if (!exceptionThrown && !finished && ReloadScreenManager.isReloadComplete()) {
            try {
                ReloadScreenManager.getCurrentReload().peek(ResourceReload::throwException);
                finished = true;
            } catch (CompletionException e) {
                exceptionThrown = true;
                exception = e;
                LOGGER.error("An exception occurred during resource loading", e);
            }
        }
        if (finished) {
            ReloadScreenManager.onFinish();
            done.run();
        }
    }

    private void renderEarly() {
        GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/title/mojang.png"));
        fill(0, 0, width, height, 0xFFFFFFFF);
        drawMojangLogoQuad((width-256)/2, (height-256)/2);
        GL11.glEnable(GL11.GL_BLEND);
        renderText(Color.BLACK, false);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * See {@link net.minecraft.client.Minecraft#method_2109(int, int, int, int, int, int)}
     */
    private void drawMojangLogoQuad(int i, int j) {
        float f = 0.00390625f;
        float f2 = 0.00390625f;
        tessellator.startQuads();
        tessellator.vertex(i, j + 256, 0.0, 0, 256 * f2);
        tessellator.vertex(i + 256, j + 256, 0.0, 256 * f, 256 * f2);
        tessellator.vertex(i + 256, j, 0.0, 256 * f, 0);
        tessellator.vertex(i, j, 0.0, 0, 0);
        tessellator.draw();
    }

    private void renderNormal(float delta) {
        parent.render(-1, -1, delta);
        this.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        renderText(Color.WHITE, true);
    }

    private void renderText(Color textColor, boolean shadow) {
        if (exceptionThrown) textRenderer.draw("Oh noes! An error occurred, check your logs.", 0,0, textColor.getRGB(), shadow);
        else textRenderer.draw("Loading resources...", 0, 0, textColor.getRGB(), shadow);
        List<String> locations = ReloadScreenManager.LOCATIONS;
        String s = locations.isEmpty() ? "Doing the do": locations.get(locations.size() - 1);
        textRenderer.draw(s, 5, height-10, textColor.getRGB(), shadow);
        String text = NUMBER_FORMAT.format(progress*100f) + "%";
        int textRendererWidth = textRenderer.getWidth(text);
        textRenderer.draw(text, width-textRendererWidth-5, height-10, textColor.getRGB(), shadow);
    }

    private void renderBackground(ToDoubleFunction<Object> deltaFunc) {
        var delta = deltaFunc.applyAsDouble(BACKGROUND_DEFAULT_DELTA_KEY);
        final int color;
        if (exceptionThrown) {
            var exceptionDelta = deltaFunc.applyAsDouble(BACKGROUND_EXCEPTION_DELTA_KEY);
            color = ColorHelper.Argb.getArgb(
                    0xFF,
                    lerp(exceptionDelta, BACKGROUND_COLOR_DEFAULT_RED, BACKGROUND_COLOR_EXCEPTION_RED),
                    lerp(exceptionDelta, BACKGROUND_COLOR_DEFAULT_GREEN, BACKGROUND_COLOR_EXCEPTION_GREEN),
                    lerp(exceptionDelta, BACKGROUND_COLOR_DEFAULT_BLUE, BACKGROUND_COLOR_EXCEPTION_BLUE)
            );
        } else color = parent == null ?
                finished ?
                        ColorHelper.Argb.getArgb(
                                0xFF,
                                lerp(delta, 0xFF, BACKGROUND_COLOR_DEFAULT_RED),
                                lerp(delta, 0xFF, BACKGROUND_COLOR_DEFAULT_GREEN),
                                lerp(delta, 0xFF, BACKGROUND_COLOR_DEFAULT_BLUE)
                        ) :
                        ColorHelper.Argb.getArgb(
                                0xFF,
                                (int) (delta * BACKGROUND_COLOR_DEFAULT_RED),
                                (int) (delta * BACKGROUND_COLOR_DEFAULT_GREEN),
                                (int) (delta * BACKGROUND_COLOR_DEFAULT_BLUE)
                        ) :
                ColorHelper.Argb.getArgb(
                        (int) (delta * 0xFF),
                        BACKGROUND_COLOR_DEFAULT_RED,
                        BACKGROUND_COLOR_DEFAULT_GREEN,
                        BACKGROUND_COLOR_DEFAULT_BLUE
                );
        fill(0, 0, width, height, color);
    }

    private void renderLogo(ToDoubleFunction<Object> deltaFunc) {
        minecraft.textureManager.bindTexture(minecraft.textureManager.getTextureId(logo));
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        tessellator.startQuads();
        tessellator.color(0xFF, 0xFF, 0xFF);
        tessellator.vertex(width / 2D - 120, (height - 90D) / 2 - 20, 0, 0, 0);
        tessellator.vertex(width / 2D - 120, (height - 90D) / 2 + 20, 0, 0, 1);
        tessellator.vertex(width / 2D + 120, (height - 90D) / 2 + 20, 0, 1, 1);
        tessellator.vertex(width / 2D + 120, (height - 90D) / 2 - 20, 0, 1, 0);
        tessellator.draw();
        glDisable(GL_BLEND);
    }

    void setTextRenderer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }
}
