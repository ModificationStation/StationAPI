package net.modificationstation.stationapi.api.client.gui.screen;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.gui.widgets.OptionButton;
import net.minecraft.client.gui.widgets.ScrollableBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.level.storage.LevelMetadata;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.EditWorldScreenEvent;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetAttachedContext;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetContextAttacher;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetDeferredDetachedContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Environment(EnvType.CLIENT)
public class EditWorldScreen extends StationScreen {

    public static final String
            SELECTWORLD_KEY = "selectWorld",
            EDIT_KEY = SELECTWORLD_KEY + "." + MODID.id("edit"),
            EDIT_TITLE_KEY = SELECTWORLD_KEY + "." + MODID.id("editTitle");
    private static final ImmutableList<ButtonWidgetDeferredDetachedContext<EditWorldScreen>> CUSTOM_EDIT_BUTTONS = StationAPI.EVENT_BUS.post(EditWorldScreenEvent.ScrollableButtonContextRegister.builder().contexts(ImmutableList.builder()).build()).contexts.build();

    protected final ScreenBase parent;
    public final LevelMetadata worldData;
    protected EditButtonList buttonMenu;

    public EditWorldScreen(ScreenBase parent, LevelMetadata worldData) {
        this.parent = parent;
        this.worldData = worldData;
    }

    @Override
    public void init() {
        super.init();
        btns.attach(
                id -> new OptionButton(id, width / 2 - 75, height - 48, I18n.translate("gui.done")),
                button -> minecraft.openScreen(parent)
        );
        buttonMenu = new EditButtonList();
        CUSTOM_EDIT_BUTTONS.stream().map(context -> context.init(this)).forEach(buttonMenu.btns::attach);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        buttonMenu.render(mouseX, mouseY, delta);
        drawTextWithShadowCentred(textManager, I18n.translate(EDIT_TITLE_KEY), width / 2, 20, Color.WHITE.hashCode());
        super.render(mouseX, mouseY, delta);
    }

    @Environment(EnvType.CLIENT)
    protected class EditButtonList extends ScrollableBase {

        protected final List<ButtonWidgetAttachedContext> editButtons = new ArrayList<>();
        protected final ButtonWidgetContextAttacher btns = ButtonWidgetContextAttacher.toList(editButtons);
        protected Button lastClickedButton;

        public EditButtonList() {
            super(minecraft, EditWorldScreen.this.width, EditWorldScreen.this.height, 32, EditWorldScreen.this.height - 64, 24);
            setDrawingSelectionBackground(false);
        }

        @Override
        protected int getSize() {
            return editButtons.size();
        }

        @Override
        protected void entryClicked(int id, boolean doubleClick) {
            ButtonWidgetAttachedContext entry = editButtons.get(id);
            Button button = entry.button();
            if (button.isMouseOver(minecraft, mouseX, mouseY)) {
                lastClickedButton = button;
                minecraft.soundHelper.playSound("random.click", 1.0f, 1.0f);
                entry.action().onPress(button);
            }
        }

        @Override
        protected boolean isEntrySelected(int i) {
            return lastClickedButton.id == i;
        }

        @Override
        protected void renderBackground() {
            EditWorldScreen.this.renderBackground();
        }

        @Override
        public void render(int mouseX, int mouseY, float delta) {
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            super.render(mouseX, mouseY, delta);
        }

        private int mouseX, mouseY;

        @Override
        protected void renderEntry(int entryId, int x, int y, int height, Tessellator tessellator) {
            Button button = editButtons.get(entryId).button();
            button.x = x + 10;
            button.y = y;
            button.render(minecraft, mouseX, mouseY);
        }
    }

}
