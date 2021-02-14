package net.modificationstation.stationapi.mixin.client;

import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.Achievements;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.util.io.StatsFileWriter;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import net.modificationstation.stationapi.api.common.achievement.AchievementPageManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static net.minecraft.achievement.Achievements.ACHIEVEMENTS;

@Mixin(Achievements.class)
public class MixinAchievements extends ScreenBase {
    @Shadow
    @Final
    private static int field_2628;
    @Shadow
    @Final
    private static int field_2630;
    @Shadow
    @Final
    private static int field_2631;
    @Shadow
    @Final
    private static int field_2629;
    @Shadow
    protected double field_2622;
    @Shadow
    protected double field_2624;
    @Shadow
    protected double field_2625;
    @Shadow
    protected double field_2623;
    @Shadow
    protected int field_2618;
    @Shadow
    protected int field_2619;
    @Shadow
    private StatsFileWriter statsFileWriter;

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At(value = "TAIL"))
    public void initPrevNext(CallbackInfo ci) {
        // Prev and next buttons.
        if (AchievementPageManager.INSTANCE.getPageCount() > 1) {
            this.buttons.add(new Button(11, this.width / 2 - 113, this.height / 2 + 74, 20, 20, "<"));
            this.buttons.add(new Button(12, this.width / 2 - 93, this.height / 2 + 74, 20, 20, ">"));
        }
    }

    @Redirect(method = "buttonClicked", at = @At(target = "Lnet/minecraft/client/gui/screen/ScreenBase;buttonClicked(Lnet/minecraft/client/gui/widgets/Button;)V", value = "INVOKE"))
    public void buttonClickedNextPrev(ScreenBase screenBase, Button button) {
        if (button.id == 11) {
            AchievementPageManager.INSTANCE.prevPage();
        } else if (button.id == 12) {
            AchievementPageManager.INSTANCE.nextPage();
        } else {
            super.buttonClicked(button);
        }
    }

    @Inject(method = "drawHeader()V", at = @At(value = "TAIL"))
    public void doDrawTitle(CallbackInfo ci) {
        if (AchievementPageManager.INSTANCE.getPageCount() > 1) {
            if (AchievementPageManager.INSTANCE.getCurrentPageName().equals("Minecraft")) {
                this.textManager.drawText("Minecraft", this.width / 2 - 69, this.height / 2 + 80, 0);
            } else {
                this.textManager.drawText(TranslationStorage.getInstance().translate("stationapi:achievementPage." + AchievementPageManager.INSTANCE.getCurrentPageName()), this.width / 2 - 69, this.height / 2 + 80, 0);
            }
        }
    }

    /**
     * @author calmilamsy
     */
    @Overwrite
    public void method_1998(int i1, int j1, float f) {

        // DRAW ACHIEVEMENT PAGE BORDER
        int k1 = MathHelper.floor(this.field_2622 + (this.field_2624 - this.field_2622) * (double) f);
        int l1 = MathHelper.floor(this.field_2623 + (this.field_2625 - this.field_2623) * (double) f);
        if (k1 < field_2628) {
            k1 = field_2628;
        }

        if (l1 < field_2629) {
            l1 = field_2629;
        }

        if (k1 >= field_2630) {
            k1 = field_2630 - 1;
        }

        if (l1 >= field_2631) {
            l1 = field_2631 - 1;
        }

        int j2 = this.minecraft.textureManager.getTextureId("/achievement/bg.png");
        int k2 = (this.width - this.field_2618) / 2;
        int l2 = (this.height - this.field_2619) / 2;
        int i3 = k2 + 16;
        int j3 = l2 + 17;
        this.zOffset = 0.0F;
        GL11.glDepthFunc(518);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glEnable('\u803a');
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);

        // DRAW ACHIEVEMENT PAGE DIRT BACKGROUND
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN).bindAtlas(this.minecraft.textureManager, 0);
        int k3 = k1 + 288 >> 4;
        int i4 = l1 + 288 >> 4;
        int j4 = (k1 + 288) % 16;
        int i5 = (l1 + 288) % 16;
        Random random = new Random();

        int ny1;
        int adjustedCol;
        int adjustedRow;
        for (ny1 = 0; ny1 * 16 - i5 < 155; ++ny1) {
            float bb1 = 0.6F - (float) (i4 + ny1) / 25.0F * 0.3F;
            GL11.glColor4f(bb1, bb1, bb1, 1.0F);

            for (adjustedCol = 0; adjustedCol * 16 - j4 < 224; ++adjustedCol) {
                random.setSeed(1234 + k3 + adjustedCol);
                random.nextInt();
                adjustedRow = AchievementPageManager.INSTANCE.getCurrentPage().getBackgroundTexture(random, k3 + adjustedCol, i4 + ny1);
                if (adjustedRow != -1) {
                    this.blit(i3 + adjustedCol * 16 - j4, j3 + ny1 * 16 - i5, adjustedRow % 16 << 4, adjustedRow >> 4 << 4, 16, 16);
                }
            }
        }

        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        GL11.glDepthFunc(515);
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);

        // DRAW LINES
        int adjustedParentCol;
        int adjustedParentRow;
        int flickerTime;
        int lineColour;
        for (Object achievementObj : ACHIEVEMENTS) {
            Achievement achievement = (Achievement) achievementObj;
            if (achievement.parent != null) {
                adjustedCol = achievement.tableColumn * 24 - k1 + 11 + i3;
                adjustedRow = achievement.tableRow * 24 - l1 + 11 + j3;
                adjustedParentCol = achievement.parent.tableColumn * 24 - k1 + 11 + i3;
                adjustedParentRow = achievement.parent.tableRow * 24 - l1 + 11 + j3;
                boolean isUnlocked = this.statsFileWriter.isAchievementUnlocked(achievement);
                boolean isUnlockable = this.statsFileWriter.isAchievementUnlockable(achievement);
                flickerTime = Math.sin((double) (System.currentTimeMillis() % 600L) / 600.0D * 3.141592653589793D * 2.0D) <= 0.6D ? 130 : 255;
                if (isUnlocked) {
                    lineColour = -9408400;
                } else if (isUnlockable) {
                    lineColour = '\uff00' + (flickerTime << 24);
                } else {
                    lineColour = -16777216;
                }

                if (this.isVisibleLine(achievement)) {
                    this.drawLineHorizontal(adjustedCol, adjustedParentCol, adjustedRow, lineColour);
                    this.drawLineVertical(adjustedParentCol, adjustedRow, adjustedParentRow, lineColour);
                }
            }
        }

        Achievement var29 = null;
        ItemRenderer var31 = new ItemRenderer();
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableLighting();
        GL11.glPopMatrix();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glEnable('\u803a');
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);

        // DRAW ACHIEVEMENT ICONS
        int var37;
        for (adjustedCol = 0; adjustedCol < ACHIEVEMENTS.size(); ++adjustedCol) {
            Achievement var32 = (Achievement) ACHIEVEMENTS.get(adjustedCol);
            if (this.isVisibleAchievement(var32)) {
                adjustedParentCol = var32.tableColumn * 24 - k1;
                adjustedParentRow = var32.tableRow * 24 - l1;
                if (adjustedParentCol >= -24 && adjustedParentRow >= -24 && adjustedParentCol <= 224 && adjustedParentRow <= 155) {
                    float var38;
                    if (this.statsFileWriter.isAchievementUnlocked(var32)) {
                        var38 = 1.0F;
                        GL11.glColor4f(var38, var38, var38, 1.0F);
                    } else if (this.statsFileWriter.isAchievementUnlockable(var32)) {
                        var38 = Math.sin((double) (System.currentTimeMillis() % 600L) / 600.0D * 3.141592653589793D * 2.0D) >= 0.6D ? 0.8F : 0.6F;
                        GL11.glColor4f(var38, var38, var38, 1.0F);
                    } else {
                        var38 = 0.3F;
                        GL11.glColor4f(var38, var38, var38, 1.0F);
                    }

                    lineColour = i3 + adjustedParentCol;
                    var37 = j3 + adjustedParentRow;

                    if (var32.isUnusual()) {
                        this.minecraft.textureManager.bindTexture(j2);
                        this.blit(lineColour - 2, var37 - 2, 26, 202, 26, 26);
                    } else {
                        this.minecraft.textureManager.bindTexture(j2);
                        this.blit(lineColour - 2, var37 - 2, 0, 202, 26, 26);
                    }

                    if (!this.statsFileWriter.isAchievementUnlockable(var32)) {
                        float var39 = 0.1F;
                        GL11.glColor4f(var39, var39, var39, 1.0F);
                        var31.field_1707 = false;
                    }

                    GL11.glEnable(2896 /*GL_LIGHTING*/);
                    GL11.glEnable(2884 /*GL_CULL_FACE*/);
                    var31.method_1487(this.minecraft.textRenderer, this.minecraft.textureManager, var32.displayItem, lineColour + 3, var37 + 3);
                    GL11.glDisable(2896 /*GL_LIGHTING*/);
                    if (!this.statsFileWriter.isAchievementUnlockable(var32)) {
                        var31.field_1707 = true;
                    }

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    if (i1 >= i3 && j1 >= j3 && i1 < i3 + 224 && j1 < j3 + 155 && i1 >= lineColour && i1 <= lineColour + 22 && j1 >= var37 && j1 <= var37 + 22) {
                        var29 = var32;
                    }
                }
            }
        }

        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(j2);
        this.blit(k2, l2, 0, 0, this.field_2618, this.field_2619);
        GL11.glPopMatrix();
        this.zOffset = 0.0F;
        GL11.glDepthFunc(515);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        super.render(i1, j1, f);

        // RENDER MOUSEOVER
        if (var29 != null) {
            String var34 = var29.NAME;
            String var35 = var29.getDescription();
            adjustedParentRow = i1 + 12;
            lineColour = j1 - 4;
            if (this.statsFileWriter.isAchievementUnlockable(var29)) {
                var37 = Math.max(this.textManager.getTextWidth(var34), 120);
                int var40 = this.textManager.method_1902(var35, var37);
                if (this.statsFileWriter.isAchievementUnlocked(var29)) {
                    var40 += 12;
                }

                this.fillGradient(adjustedParentRow - 3, lineColour - 3, adjustedParentRow + var37 + 3, lineColour + var40 + 3 + 12, -1073741824, -1073741824);
                this.textManager.method_1904(var35, adjustedParentRow, lineColour + 12, var37, -6250336);
                if (this.statsFileWriter.isAchievementUnlocked(var29)) {
                    try {
                        this.textManager.drawTextWithShadow(TranslationStorage.getInstance().translate("achievement.taken"), adjustedParentRow, lineColour + var40 + 4, -7302913);
                    } catch (Exception var28) {
                        var28.printStackTrace();
                    }
                }
            } else {
                try {
                    var37 = Math.max(this.textManager.getTextWidth(var34), 120);
                    String var41 = TranslationStorage.getInstance().translate("achievement.requires", var29.parent.NAME);
                    flickerTime = this.textManager.method_1902(var41, var37);
                    this.fillGradient(adjustedParentRow - 3, lineColour - 3, adjustedParentRow + var37 + 3, lineColour + flickerTime + 12 + 3, -1073741824, -1073741824);
                    this.textManager.method_1904(var41, adjustedParentRow, lineColour + 12, var37, -9416624);
                } catch (Exception var27) {
                    var27.printStackTrace();
                }
            }

            this.textManager.drawTextWithShadow(var34, adjustedParentRow, lineColour, this.statsFileWriter.isAchievementUnlockable(var29) ? (var29.isUnusual() ? -128 : -1) : (var29.isUnusual() ? -8355776 : -8355712));
        }

        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        RenderHelper.disableLighting();
    }

    public boolean isVisibleAchievement(Achievement achievement) {
        if (this.checkHidden(achievement)) {
            return false;
        } else if (!AchievementPageManager.INSTANCE.getCurrentPage().getAchievementIds().contains(achievement.ID)) {
            return false;
        } else if (achievement.parent != null && !checkHidden(achievement.parent)) {
            return true;
        } else {
            return true;
        }
    }


    public boolean isVisibleLine(Achievement achievement) {
        return achievement.parent != null && isVisibleAchievement(achievement) && isVisibleAchievement(achievement.parent);
    }

    public boolean checkHidden(Achievement achievement) {
        if (minecraft.statFileWriter.isAchievementUnlocked(achievement)) {
            return false;
        }
        if (achievement.parent == null) {
            return false;
        } else {
            return checkHidden(achievement.parent);
        }
    }
}
