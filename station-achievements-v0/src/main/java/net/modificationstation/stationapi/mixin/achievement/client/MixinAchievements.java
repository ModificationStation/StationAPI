package net.modificationstation.stationapi.mixin.achievement.client;

import net.minecraft.achievement.Achievement;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.Achievements;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.menu.AchievementsEvent;
import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

import static net.minecraft.achievement.Achievements.ACHIEVEMENTS;

@Mixin(Achievements.class)
public class MixinAchievements extends ScreenBase {

    @SuppressWarnings("unchecked")
    @Inject(method = "init", at = @At(value = "TAIL"))
    public void initPrevNext(CallbackInfo ci) {
        // Prev and next buttons.
        if (AchievementPage.getPageCount() > 1) {
            this.buttons.add(new Button(11, this.width / 2 - 113, this.height / 2 + 74, 20, 20, "<"));
            this.buttons.add(new Button(12, this.width / 2 - 93, this.height / 2 + 74, 20, 20, ">"));
        }
    }

    @Redirect(method = "buttonClicked", at = @At(target = "Lnet/minecraft/client/gui/screen/ScreenBase;buttonClicked(Lnet/minecraft/client/gui/widgets/Button;)V", value = "INVOKE"))
    public void buttonClickedNextPrev(ScreenBase screenBase, Button button) {
        if (button.id == 11) {
            AchievementPage.prevPage();
        } else if (button.id == 12) {
            AchievementPage.nextPage();
        } else {
            super.buttonClicked(button);
        }
    }

    @Inject(method = "drawHeader()V", at = @At(value = "TAIL"))
    public void doDrawTitle(CallbackInfo ci) {
        if (AchievementPage.getPageCount() > 1) {
            if (AchievementPage.getCurrentPageName().equals("Minecraft")) {
                this.textManager.drawText("Minecraft", this.width / 2 - 69, this.height / 2 + 80, 0);
            } else {
                this.textManager.drawText(I18n.translate("stationapi:achievementPage." + AchievementPage.getCurrentPageName()), this.width / 2 - 69, this.height / 2 + 80, 0);
            }
        }
    }

    @Inject(method = "method_1998(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/block/BlockBase;texture:I", opcode = Opcodes.GETFIELD, ordinal = 7, shift = At.Shift.BY, by = 3), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureLocals(int mouseX, int mouseY, float delta, CallbackInfo ci, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15, Random var21, int var22, float var23, int var24, int var25) {
        capturedRandom = var21;
        capturedColumn = var12 + var24;
        capturedRow = var13 + var22;
        capturedRowRandomized = var25;
    }

    private Random capturedRandom;
    private int capturedColumn;
    private int capturedRow;
    private int capturedRowRandomized;

    @ModifyVariable(method = "method_1998(IIF)V", index = 26, at = @At(value = "FIELD", target = "Lnet/minecraft/block/BlockBase;texture:I", opcode = Opcodes.GETFIELD, ordinal = 7, shift = At.Shift.BY, by = 3))
    private int renderBackgroundTexture(int var26) {
        return StationAPI.EVENT_BUS.post(new AchievementsEvent.BackgroundTextureRender((Achievements) (Object) this, capturedRandom, capturedColumn, capturedRow, capturedRowRandomized, var26)).backgroundTexture;
    }

    @Redirect(method = "method_1998(IIF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/achievement/Achievement;parent:Lnet/minecraft/achievement/Achievement;", opcode = Opcodes.GETFIELD, ordinal = 0))
    private Achievement overrideLineRender(Achievement achievement) {
        return StationAPI.EVENT_BUS.post(new AchievementsEvent.LineRender((Achievements) (Object) this, achievement)).isCancelled() ? null : achievement.parent;
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @ModifyConstant(method = "method_1998(IIF)V", constant = @Constant(intValue = 0, ordinal = 4))
    private int onForLoopStart(int i) {
        return onRenderAchievement(i);
    }

    @ModifyVariable(method = "method_1998(IIF)V", index = 14, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V", ordinal = 4, shift = At.Shift.BY, by = -4, remap = false))
    private int onNextForIteration(int i) {
        return onRenderAchievement(i);
    }

    private int onRenderAchievement(int achievementOrdinal) {
        while (achievementOrdinal < ACHIEVEMENTS.size() && StationAPI.EVENT_BUS.post(new AchievementsEvent.AchievementIconRender((Achievements) (Object) this, (Achievement) ACHIEVEMENTS.get(achievementOrdinal))).isCancelled())
            achievementOrdinal++;
        return achievementOrdinal;
    }
}
