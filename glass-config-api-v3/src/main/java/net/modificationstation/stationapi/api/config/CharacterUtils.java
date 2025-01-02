package net.modificationstation.stationapi.api.config;

import com.google.common.base.CharMatcher;
import net.modificationstation.stationapi.impl.config.DrawContextAccessor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.awt.datatransfer.*;
import java.util.List;

/**
 * Some utility methods copied over from r1.2.5 for use in ExtensibleTextFieldWidget.
 * This should be useful for other things.
 */
public class CharacterUtils {
    // Custom methods

    /**
     * Renders a tooltip on screen at the provided place, handling flipping of the tooltip where required.
     * @param textRenderer the text renderer to use. You can use <code>((Minecraft) FabricLoader.getInstance().getGameInstance()).textRenderer</code> for this.
     * @param tooltip the tooltip to render. Can be multiline using multiple elements on the list.
     * @param x the X position where the tooltip should be. Typically mouseX.
     * @param y the Y position where the tooltip should be. Typically mouseY.
     * @param screen the screen where the tooltip is being rendered.
     */
    public static void renderTooltip(TextRenderer textRenderer, List<String> tooltip, int x, int y, Screen screen) {
        if (!tooltip.isEmpty()) {

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;

            for (String string : tooltip) {
                int l = textRenderer.getWidth(string);
                if (l > k) {
                    k = l;
                }
            }

            int m = x + 12;
            int n = y - 12;
            int p = 8;
            if (tooltip.size() > 1) {
                p += 2 + (tooltip.size() - 1) * 10;
            }

            if (m + k > screen.width) {
                m -= 28 + k;
            }

            if (n + p + 6 > screen.height) {
                n = screen.height - p - 6;
            }

            int transparentGrey = -1073741824;
            int margin = 3;
            ((DrawContextAccessor) screen).invokeFill(m - margin, n - margin, m + k + margin,
                    n + p + margin, transparentGrey);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 300);

            for(int t = 0; t < tooltip.size(); ++t) {
                String string2 = tooltip.get(t);
                if (string2 != null) {
                    textRenderer.draw(string2, m, n, 0xffffff);
                }

                if (t == 0) {
                    n += 2;
                }

                n += 10;
            }

            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    /**
     * Custom function for converting an JWJGL colour into minecraft's weird ARGB system.
     * Uses AWT's Colour class because LWJGL's one doesn't exist on server, so it saves me a headache.
     */
    public static int getIntFromColour(Color colour) {
        return ((colour.getAlpha() & 255) << 24) | ((colour.getRed() & 255) << 16) | ((colour.getGreen() & 255) << 8) | (colour.getBlue() & 255);
    }

    /**
     * Susceptible to overflows, but honestly, I am not too concerned.
     * <a href="https://stackoverflow.com/a/237204">StackOverflow Source</a>
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * Susceptible to overflows, but honestly, I am not too concerned. Modified to look for floats instead.
     * <a href="https://stackoverflow.com/a/237204">StackOverflow Source</a>
     */
    public static boolean isFloat(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0 || str.charAt(0) == '.' || str.charAt(str.length()-1) == '.' || CharMatcher.is('.').countIn(str) > 1) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if ((c < '0' || c > '9') && c != '.') {
                return false;
            }
        }
        return true;
    }

    // 1.2.5 methods. Most of these I have no real explanation for.

    public static boolean isCharacterValid(char c) {
        return c != 167 && (net.minecraft.util.CharacterUtils.VALID_CHARACTERS.indexOf(c) >= 0 || c > ' ');
    }

    public static String stripInvalidChars(String string) {
        StringBuilder var1 = new StringBuilder();
        char[] var2 = string.toCharArray();

        for (char var5 : var2) {
            if (isCharacterValid(var5)) {
                var1.append(var5);
            }
        }

        return var1.toString();
    }

    /**
     * Tries to set the given string as clipboard text. Silently fails.
     * @param string the text to set.
     */
    public static void setClipboardText(String string) {
        try {
            StringSelection var1 = new StringSelection(string);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(var1, null);
        } catch (Exception ignored) {
        }

    }

    /**
     * Gets the current text on clipboard. Strips formatting.
     * @return the current text of the clipboard. Can be empty, but never null. Fails silently.
     */
    public static String getClipboardText() {
        try {
            Transferable var0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (var0 != null && var0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String)var0.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception ignored) {
        }

        return "";
    }

    /**
     * Processes string colours. I think. I have no idea, honestly.
     */
    public static String getRenderableString(String string, int maxPixelWidth, boolean flag, TextRenderer textRenderer) {
        StringBuilder var4 = new StringBuilder();
        int currentPixelWidth = 0;
        int var6 = flag ? string.length() - 1 : 0;
        int var7 = flag ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;

        for(int var10 = var6; var10 >= 0 && var10 < string.length() && currentPixelWidth < maxPixelWidth; var10 += var7) {
            char var11 = string.charAt(var10);
            int var12 = textRenderer.getWidth(Character.toString(var11));
            if (var8) {
                var8 = false;
                if (var11 != 'l' && var11 != 'L') {
                    if (var11 == 'r' || var11 == 'R') {
                        var9 = false;
                    }
                } else {
                    var9 = true;
                }
            } else if (var12 < 0) {
                var8 = true;
            } else {
                currentPixelWidth += var12;
                if (var9) {
                    ++currentPixelWidth;
                }
            }

            if (currentPixelWidth > maxPixelWidth) {
                break;
            }

            if (flag) {
                var4.insert(0, var11);
            } else {
                var4.append(var11);
            }
        }

        return var4.toString();
    }
}
