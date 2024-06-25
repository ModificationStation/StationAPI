package net.modificationstation.stationapi.impl.config.screen.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.config.CharacterUtils;
import net.modificationstation.stationapi.api.config.HasDrawable;
import net.modificationstation.stationapi.api.config.HasToolTip;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.awt.*;
import java.util.List;
import java.util.function.*;

/**
 * Basically a modified Textbox from r1.2.5, but modified for gcapi's use case.
 */
public class ExtensibleTextFieldWidget extends DrawContext implements HasDrawable, HasToolTip {

    private static final int serverSyncedBorder = CharacterUtils.getIntFromColour(new Color(255, 202, 0, 255));
    private static final int serverSyncedText = CharacterUtils.getIntFromColour(new Color(170, 139, 21, 255));
    private final TextRenderer textRenderer;
    private int x;
    private int y;
    private int width;
    private int height;
    private String text = "";
    private int maxLength = 32;
    private int focusedTicks;
    private boolean shouldDrawBackground = true;
    private boolean enabled = true;
    private boolean selected = false;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean focusable = true;
    private int cursorPosition = 0;
    private int cursorMax = 0;
    private int cursorMin = 0;
    public int selectedTextColour = 14737632;
    public int deselectedTextColour = 7368816;
    public int errorBorderColour = CharacterUtils.getIntFromColour(new Color(200, 50, 50));

    private boolean doRenderUpdate = true;
    private Function<String, List<String>> contentsValidator;

    public ExtensibleTextFieldWidget(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public boolean isValueValid() {
        if (contentsValidator != null) {
            return contentsValidator.apply(getText()) == null;
        }
        return true;
    }

    @Override
    public void tick() {
        ++this.focusedTicks;
    }

    public void setText(String string) {
        if (string.length() > this.maxLength) {
            this.text = string.substring(0, this.maxLength);
        } else {
            this.text = string;
        }

        this.onTextChanged();
    }

    public String getText() {
        return this.text;
    }

    public String getSelectedText() {
        int var1 = Math.min(this.cursorMax, this.cursorMin);
        int var2 = Math.max(this.cursorMax, this.cursorMin);
        return this.text.substring(var1, var2);
    }

    public void addText(String string) {
        String var2 = "";
        String var3 = CharacterUtils.stripInvalidChars(string);
        int var4 = Math.min(this.cursorMax, this.cursorMin);
        int var5 = Math.max(this.cursorMax, this.cursorMin);
        int var6 = this.maxLength - this.text.length() - (var4 - this.cursorMin);
        if (this.text.length() > 0) {
            var2 = var2 + this.text.substring(0, var4);
        }

        int var8;
        if (var6 < var3.length()) {
            var2 = var2 + var3.substring(0, var6);
            var8 = var6;
        } else {
            var2 = var2 + var3;
            var8 = var3.length();
        }

        if (this.text.length() > 0 && var5 < this.text.length()) {
            var2 = var2 + this.text.substring(var5);
        }

        this.text = var2;
        this.updateOffsetCursorMax(var4 - this.cursorMin + var8);
    }

    public void method_729(int i) {
        if (this.text.length() != 0) {
            if (this.cursorMin != this.cursorMax) {
                this.addText("");
            } else {
                this.method_735(this.method_739(i) - this.cursorMax);
            }
        }
    }

    public void method_735(int i) {
        if (this.text.length() != 0) {
            if (this.cursorMin != this.cursorMax) {
                this.addText("");
            } else {
                boolean var2 = i < 0;
                int var3 = var2 ? this.cursorMax + i : this.cursorMax;
                int var4 = var2 ? this.cursorMax : this.cursorMax + i;
                String var5 = "";
                if (var3 >= 0) {
                    var5 = this.text.substring(0, var3);
                }

                if (var4 < this.text.length()) {
                    var5 = var5 + this.text.substring(var4);
                }

                this.text = var5;
                if (var2) {
                    this.updateOffsetCursorMax(i);
                }

            }
        }
    }

    public int method_739(int i) {
        return this.method_730(i, this.getCursorMax());
    }

    public int method_730(int i, int j) {
        int var3 = j;
        boolean var4 = i < 0;
        int var5 = Math.abs(i);

        for(int var6 = 0; var6 < var5; ++var6) {
            if (!var4) {
                int var7 = this.text.length();
                var3 = this.text.indexOf(32, var3);
                if (var3 == -1) {
                    var3 = var7;
                } else {
                    while(var3 < var7 && this.text.charAt(var3) == ' ') {
                        ++var3;
                    }
                }
            } else {
                while(var3 > 0 && this.text.charAt(var3 - 1) == ' ') {
                    --var3;
                }

                while(var3 > 0 && this.text.charAt(var3 - 1) != ' ') {
                    --var3;
                }
            }
        }

        return var3;
    }

    public void updateOffsetCursorMax(int cursorMax) {
        this.updateCursorMax(this.cursorMin + cursorMax);
    }

    public void updateCursorMax(int cursorMax) {
        this.cursorMax = cursorMax;
        int var2 = this.text.length();
        if (this.cursorMax < 0) {
            this.cursorMax = 0;
        }

        if (this.cursorMax > var2) {
            this.cursorMax = var2;
        }

        this.updateCursorPosition(this.cursorMax);
    }

    public void updateCursorMax() {
        this.updateCursorMax(0);
    }

    public void onTextChanged() {
        this.updateCursorMax(this.text.length());
    }

    @Override
    public void keyPressed(char c, int i) {
        if (this.focusable && this.selected) {
            switch(c) {
                case '\u0001':
                    this.onTextChanged();
                    this.updateCursorPosition(0);
                    return;
                case '\u0003':
                    CharacterUtils.setClipboardText(this.getSelectedText());
                    return;
                case '\u0016':
                    this.addText(CharacterUtils.getClipboardText());
                    return;
                case '\u0018':
                    CharacterUtils.setClipboardText(this.getSelectedText());
                    this.addText("");
                    return;
                default:
                    switch(i) {
                        case 14:
                            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                this.method_729(-1);
                            } else {
                                this.method_735(-1);
                            }

                            return;
                        case 199:
                            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                this.updateCursorPosition(0);
                            } else {
                                this.updateCursorMax();
                            }

                            return;
                        case 203:
                            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                                    this.updateCursorPosition(this.method_730(-1, this.getCursorMin()));
                                } else {
                                    this.updateCursorPosition(this.getCursorMin() - 1);
                                }
                            } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                                this.updateCursorMax(this.method_739(-1));
                            } else {
                                this.updateOffsetCursorMax(-1);
                            }

                            return;
                        case 205:
                            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                                    this.updateCursorPosition(this.method_730(1, this.getCursorMin()));
                                } else {
                                    this.updateCursorPosition(this.getCursorMin() + 1);
                                }
                            } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                                this.updateCursorMax(this.method_739(1));
                            } else {
                                this.updateOffsetCursorMax(1);
                            }

                            return;
                        case 207:
                            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                this.updateCursorPosition(this.text.length());
                            } else {
                                this.onTextChanged();
                            }

                            return;
                        case 211:
                            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                                this.method_729(1);
                            } else {
                                this.method_735(1);
                            }

                            return;
                        default:
                            if (CharacterUtils.isCharacterValid(c)) {
                                this.addText(Character.toString(c));
                            }
                    }
            }
        }
    }

    @Override
    public void setID(int id) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean isMouseHovering = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
        if (this.enabled) {
            this.setSelected(this.focusable && isMouseHovering);
        }

        if (this.selected && button == 0) {
            int var5 = mouseX - this.x;
            if (this.shouldDrawBackground) {
                var5 -= 4;
            }

            String var6 = CharacterUtils.getRenderableString(this.text.substring(this.cursorPosition), this.getBackgroundOffset(), false, textRenderer);
            this.updateCursorMax(CharacterUtils.getRenderableString(var6, var5, false, textRenderer).length() + this.cursorPosition);
        }

    }

    @Override
    public void setXYWH(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public List<String> getTooltip() {
        if (contentsValidator != null) {
            return contentsValidator.apply(getText());
        }
        return null;
    }

    @Override
    public int[] getXYWH() {
        return new int[]{x, y, width, height};
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (doRenderUpdate) {
            onTextChanged();
            doRenderUpdate = false;
        }
        if (this.shouldDrawBackground()) {
            fill(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, isValueValid()? enabled? -6250336 : serverSyncedBorder : errorBorderColour);
            fill(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
        }

        int var1 = this.focusable ? enabled? this.selectedTextColour : serverSyncedText : this.deselectedTextColour;
        int var2 = this.cursorMax - this.cursorPosition;
        int var3 = this.cursorMin - this.cursorPosition;
        String var4 = CharacterUtils.getRenderableString(this.text.substring(this.cursorPosition), this.getBackgroundOffset(), false, textRenderer);
        boolean var5 = var2 >= 0 && var2 <= var4.length();
        boolean var6 = this.selected && this.focusedTicks / 6 % 2 == 0 && var5;
        int firstStringPos = this.shouldDrawBackground ? this.x + 4 : this.x;
        int textY = this.shouldDrawBackground ? this.y + (this.height - 8) / 2 : this.y;
        int secondStringPos = firstStringPos;
        if (var3 > var4.length()) {
            var3 = var4.length();
        }

        if (var4.length() > 0) {
            String firstString = var5 ? var4.substring(0, var2) : var4;
            this.textRenderer.drawWithShadow(firstString, firstStringPos, textY, var1);
            secondStringPos += textRenderer.getWidth(firstString);
            secondStringPos++;
        }

        boolean var13 = this.cursorMax < this.text.length() || this.text.length() >= this.getMaxLength();
        int selectStart = secondStringPos;
        if (!var5) {
            selectStart = var2 > 0 ? firstStringPos + this.width : firstStringPos;
        } else if (var13) {
            selectStart = --secondStringPos;
        }

        if (var4.length() > 0 && var5 && var2 < var4.length()) {
            this.textRenderer.drawWithShadow(var4.substring(var2), secondStringPos, textY, var1);
        }

        if (var6) {
            if (var13) {
                fill(selectStart, textY - 1, selectStart + 1, textY + (this.height /2) - 2, -3092272);
            } else {
                this.textRenderer.drawWithShadow("_", selectStart, textY, var1);
            }
        }

        if (var3 != var2) {
            int var12 = firstStringPos + this.textRenderer.getWidth(var4.substring(0, var3));
            this.drawHighlightOverlay(selectStart, textY - 1, var12 - 1, textY + (this.height /2));
        }

    }

    private void drawHighlightOverlay(int x, int y, int width, int height) {
        int topLeftCorner;
        if (x < width) {
            topLeftCorner = x;
            x = width;
            width = topLeftCorner;
        }

        if (y < height) {
            topLeftCorner = y;
            y = height;
            height = topLeftCorner;
        }

        Tessellator var6 = Tessellator.INSTANCE;
        GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
        GL11.glDisable(3553);
        GL11.glEnable(3058);
        GL11.glLogicOp(5387);
        var6.startQuads();
        var6.vertex(x, height, 0.0D);
        var6.vertex(width, height, 0.0D);
        var6.vertex(width, y, 0.0D);
        var6.vertex(x, y, 0.0D);
        var6.draw();
        GL11.glDisable(3058);
        GL11.glEnable(3553);
    }

    public void setMaxLength(int i) {
        this.maxLength = i;
        if (this.text.length() > i) {
            this.text = this.text.substring(0, i);
        }

    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public int getCursorMax() {
        return this.cursorMax;
    }

    public boolean shouldDrawBackground() {
        return this.shouldDrawBackground;
    }

    @SuppressWarnings("unused")
    public void setShouldDrawBackground(boolean flag) {
        this.shouldDrawBackground = flag;
    }

    public void setSelected(boolean flag) {
        if (flag && !this.selected) {
            this.focusedTicks = 0;
        }

        this.selected = flag;
    }

    @SuppressWarnings("unused")
    public boolean isSelected() {
        return this.selected;
    }

    public int getCursorMin() {
        return this.cursorMin;
    }

    public int getBackgroundOffset() {
        return this.shouldDrawBackground() ? this.width - 8 : this.width;
    }

    public void updateCursorPosition(int newCursorPos) {
        int var2 = this.text.length();
        if (newCursorPos > var2) {
            newCursorPos = var2;
        }

        if (newCursorPos < 0) {
            newCursorPos = 0;
        }

        this.cursorMin = newCursorPos;
        if (this.textRenderer != null) {
            if (this.cursorPosition > var2) {
                this.cursorPosition = var2;
            }

            int backgroundOffset = this.getBackgroundOffset();
            String visibleString = CharacterUtils.getRenderableString(this.text.substring(this.cursorPosition), backgroundOffset, false, textRenderer);
            int var5 = visibleString.length() + this.cursorPosition;
            if (newCursorPos == this.cursorPosition) {
                this.cursorPosition -= CharacterUtils.getRenderableString(this.text, backgroundOffset, true, textRenderer).length();
            }

            if (newCursorPos > var5) {
                this.cursorPosition += newCursorPos - var5;
            } else if (newCursorPos <= this.cursorPosition) {
                this.cursorPosition -= this.cursorPosition - newCursorPos;
            }

            if (this.cursorPosition < 0) {
                this.cursorPosition = 0;
            }

            if (this.cursorPosition > var2) {
                this.cursorPosition = var2;
            }
        }

    }

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setValidator(Function<String, List<String>> contentsValidator) {
        this.contentsValidator = contentsValidator;
    }
}
