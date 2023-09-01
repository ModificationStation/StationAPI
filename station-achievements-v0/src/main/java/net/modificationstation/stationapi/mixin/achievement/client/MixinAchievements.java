package net.modificationstation.stationapi.mixin.achievement.client;

import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static net.minecraft.achievement.Achievements.ACHIEVEMENTS;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Mixin(Achievements.class)
class MixinAchievements extends ScreenBase {
    @Unique
    private static final int
            STATIONAPI$PREV_BUTTON_ID = MODID.id("prev").hashCode(),
            STATIONAPI$NEXT_BUTTON_ID = MODID.id("next").hashCode();


    @SuppressWarnings("unchecked")
    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    private void stationapi_initPrevNext(CallbackInfo ci) {
        // Prev and next buttons.
        if (AchievementPage.getPageCount() <= 1) return;
        this.buttons.add(new Button(STATIONAPI$PREV_BUTTON_ID, this.width / 2 - 113, this.height / 2 + 74, 20, 20, "<"));
        this.buttons.add(new Button(STATIONAPI$NEXT_BUTTON_ID, this.width / 2 - 93, this.height / 2 + 74, 20, 20, ">"));
    }

    @Inject(
            method = "buttonClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ScreenBase;buttonClicked(Lnet/minecraft/client/gui/widgets/Button;)V"
            )
    )
    private void stationapi_buttonClickedNextPrev(Button button, CallbackInfo ci) {
        if (button.id == STATIONAPI$PREV_BUTTON_ID) AchievementPage.prevPage();
        else if (button.id == STATIONAPI$NEXT_BUTTON_ID) AchievementPage.nextPage();
    }

    @Inject(
            method = "drawHeader",
            at = @At("TAIL")
    )
    private void stationapi_doDrawTitle(CallbackInfo ci) {
        if (AchievementPage.getPageCount() > 1) this.textManager.drawText(
                I18n.translate("stationapi:achievementPage." + AchievementPage.getCurrentPageName()),
                this.width / 2 - 69, this.height / 2 + 80, 0
        );
    }

    @ModifyVariable(
            method = "method_1998",
            index = 26,
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;texture:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 7,
                    shift = At.Shift.BY,
                    by = 3
            )
    )
    private int stationapi_renderBackgroundTexture(
            int var26,
            @Local(index = 21) Random random,
            @Local(index = 12) int baseColumn, @Local(index = 24) int deltaColumn,
            @Local(index = 13) int baseRow, @Local(index = 22) int deltaRow,
            @Local(index = 25) int randomizedRow
    ) {
        //noinspection DataFlowIssue
        return StationAPI.EVENT_BUS.post(
                AchievementsEvent.BackgroundTextureRender.builder()
                        .achievementsScreen((Achievements) (Object) this)
                        .random(random)
                        .column(baseColumn + deltaColumn)
                        .row(baseRow + deltaRow)
                        .randomizedRow(randomizedRow)
                        .backgroundTexture(var26)
                        .build()
        ).backgroundTexture;
    }

    @Redirect(
            method = "method_1998",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/achievement/Achievement;parent:Lnet/minecraft/achievement/Achievement;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    private Achievement stationapi_overrideLineRender(Achievement achievement) {
        //noinspection DataFlowIssue
        return StationAPI.EVENT_BUS.post(
                AchievementsEvent.LineRender.builder()
                        .achievementsScreen((Achievements) (Object) this)
                        .achievement(achievement)
                        .build()
        ).isCanceled() ? null : achievement.parent;
    }

    @ModifyConstant(
            method = "method_1998",
            constant = @Constant(
                    intValue = 0,
                    ordinal = 4
            )
    )
    private int stationapi_onForLoopStart(int i) {
        return stationapi_onRenderAchievement(i);
    }

    @ModifyVariable(
            method = "method_1998",
            index = 14,
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V",
                    ordinal = 4,
                    shift = At.Shift.BY,
                    by = -4,
                    remap = false
            )
    )
    private int stationapi_onNextForIteration(int i) {
        return stationapi_onRenderAchievement(i);
    }

    @Unique
    private int stationapi_onRenderAchievement(int achievementOrdinal) {
        //noinspection DataFlowIssue
        while (
                achievementOrdinal < ACHIEVEMENTS.size() && StationAPI.EVENT_BUS.post(
                        AchievementsEvent.AchievementIconRender.builder()
                                .achievementsScreen((Achievements) (Object) this)
                                .achievement((Achievement) ACHIEVEMENTS.get(achievementOrdinal))
                                .build()
                ).isCanceled()
        )
            achievementOrdinal++;
        return achievementOrdinal;
    }
}
