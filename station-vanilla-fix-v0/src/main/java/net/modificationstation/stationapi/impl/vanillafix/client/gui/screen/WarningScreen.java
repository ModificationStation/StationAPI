package net.modificationstation.stationapi.impl.vanillafix.client.gui.screen;

import lombok.RequiredArgsConstructor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.client.gui.screen.StationScreen;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetAttachedContext;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@RequiredArgsConstructor
public class WarningScreen extends StationScreen {

    public static final String
            ROOT_KEY = MODID.id("warning").toString(),
            WARNING_KEY = ROOT_KEY + "." + MODID.id("warning");

    private static final int CONFIRMATION_TIMEOUT = FabricLoader.getInstance().isDevelopmentEnvironment() ? 3000 : 25000;

    private final Screen parent;
    private final Runnable action;
    private final String
            explanationKey,
            confirmKey;
    private ButtonWidgetAttachedContext confirmButton;
    private final long createdTimestamp = System.currentTimeMillis();

    @Override
    public void init() {
        super.init();
        confirmButton = btns.attach(
                id -> {
                    ButtonWidget button = new ButtonWidget(id, width / 2 - 100, height - height / 10 - 44, I18n.getTranslation(confirmKey));
                    button.active = false;
                    return button;
                },
                button -> action.run()
        );
        btns.attach(
                id -> new ButtonWidget(id, width / 2 - 100, height - height / 10 - 20, I18n.getTranslation("gui.cancel")),
                button -> minecraft.setScreen(parent)
        );
    }

    private void renderDirtBackground(long time) {
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float f = 32.0f;
        int color = 48 + (int) (MathHelper.abs(MathHelper.sin((float) ((time % 50000L) / 50000D * Math.PI * 2))) * 32);
        color = color << 16 | color << 8 | color;
        double offsetU = MathHelper.sin((float) ((time % 30000L) / 30000D * Math.PI * 2)) * 10;
        double offsetV = MathHelper.sin((float) ((time % 5000L) / 5000D * Math.PI * 2));
        tessellator.startQuads();
        tessellator.color(color);
        tessellator.vertex(0.0, height, 0.0, offsetU, height / f + offsetV);
        tessellator.vertex(width, height, 0.0, width / f + offsetU, height / f + offsetV);
        tessellator.vertex(width, 0.0, 0.0, width / f + offsetU, offsetV);
        tessellator.vertex(0.0, 0.0, 0.0, offsetU, offsetV);
        tessellator.draw();
    }

    @Override
    public void render(int i, int j, float f) {
        long time = System.currentTimeMillis();
        renderDirtBackground(time);
        GL11.glPushMatrix();
        String warning = I18n.getTranslation(WARNING_KEY);
        double warningScale = 2 + MathHelper.sin((float) ((time % 5000L) / 5000D * Math.PI * 2)) * 0.1F;
        GL11.glTranslated(width / 2D - textRenderer.getWidth(warning) * warningScale / 2D, height / 10D, 0);
        GL11.glScaled(warningScale, warningScale, 1);
        drawTextWithShadow(textRenderer, warning, 0, 0, Color.YELLOW.hashCode());
        GL11.glPopMatrix();
        String[] info = I18n.getTranslation(explanationKey).split("\\n");
        int curHeight = height / 3;
        for (String line : info) {
            int lineWidth = textRenderer.getWidth(line);
            if (lineWidth > width - 20) {
                int begin = 0;
                int lastSpace = -1;
                for (int cur = 0, lineLength = line.length(); cur < lineLength; cur++) {
                    boolean isSpace = line.charAt(cur) == ' ';
                    boolean isEnd = cur + 1 == lineLength;
                    if (isSpace || isEnd) {
                        String newLine = isEnd ? line.substring(begin) : line.substring(begin, cur);
                        int newLineWidth = textRenderer.getWidth(newLine);
                        if (newLineWidth > width - 20) {
                            drawCenteredTextWithShadow(textRenderer, line.substring(begin, lastSpace), width / 2, curHeight, Color.WHITE.hashCode());
                            curHeight += 10;
                            begin = lastSpace + 1;
                        }
                        if (isSpace)
                            lastSpace = cur;
                        if (isEnd) {
                            drawCenteredTextWithShadow(textRenderer, line.substring(begin), width / 2, curHeight, Color.WHITE.hashCode());
                            curHeight += 10;
                        }
                    }
                }
            } else {
                drawCenteredTextWithShadow(textRenderer, line, width / 2, curHeight, Color.WHITE.hashCode());
                curHeight += 10;
            }
        }
        long confirmationTimeout = time - createdTimestamp;
        if (confirmationTimeout > CONFIRMATION_TIMEOUT) {
            if (!confirmButton.button().active) {
                confirmButton.button().active = true;
                confirmButton.button().text = I18n.getTranslation(confirmKey);
            }
        } else confirmButton.button().text = I18n.getTranslation(confirmKey) + " (" + (CONFIRMATION_TIMEOUT - confirmationTimeout) / 1000 + "s)";
        super.render(i, j, f);
    }
}
