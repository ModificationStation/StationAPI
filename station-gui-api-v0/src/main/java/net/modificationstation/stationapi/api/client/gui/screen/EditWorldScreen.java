package net.modificationstation.stationapi.api.client.gui.screen;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_591;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.EditWorldScreenEvent;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetAttachedContext;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetContextAttacher;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetDeferredDetachedContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Environment(EnvType.CLIENT)
public class EditWorldScreen extends StationScreen {

    public static final String
            SELECTWORLD_KEY = "selectWorld",
            EDIT_KEY = SELECTWORLD_KEY + "." + NAMESPACE + ".edit",
            EDIT_TITLE_KEY = SELECTWORLD_KEY + "." + NAMESPACE + ".editTitle";
    private static final ImmutableList<ButtonWidgetDeferredDetachedContext<EditWorldScreen>> CUSTOM_EDIT_BUTTONS = StationAPI.EVENT_BUS.post(EditWorldScreenEvent.ScrollableButtonContextRegister.builder().contexts(ImmutableList.builder()).build()).contexts.build();

    protected final Screen parent;
    public final class_591 worldData;
    protected EditButtonList buttonMenu;

    public EditWorldScreen(Screen parent, class_591 worldData) {
        this.parent = parent;
        this.worldData = worldData;
    }

    @Override
    public void init() {
        super.init();
        btns.attach(
                id -> new OptionButtonWidget(id, width / 2 - 75, height - 48, I18n.getTranslation("gui.done")),
                button -> minecraft.setScreen(parent)
        );
        buttonMenu = new EditButtonList();
        CUSTOM_EDIT_BUTTONS.stream().map(context -> context.init(this)).forEach(buttonMenu.btns::attach);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        buttonMenu.render(mouseX, mouseY, delta);
        drawCenteredTextWithShadow(textRenderer, I18n.getTranslation(EDIT_TITLE_KEY), width / 2, 20, Color.WHITE.hashCode());
        super.render(mouseX, mouseY, delta);
    }

    @Environment(EnvType.CLIENT)
    protected class EditButtonList extends EntryListWidget {

        protected final List<ButtonWidgetAttachedContext> editButtons = new ArrayList<>();
        protected final ButtonWidgetContextAttacher btns = ButtonWidgetContextAttacher.toList(editButtons);
        protected ButtonWidget lastClickedButton;

        public EditButtonList() {
            super(minecraft, EditWorldScreen.this.width, EditWorldScreen.this.height, 32, EditWorldScreen.this.height - 64, 24);
            method_1260(false);
        }

        @Override
        protected int getEntryCount() {
            return editButtons.size();
        }

        @Override
        protected void entryClicked(int id, boolean doubleClick) {
            ButtonWidgetAttachedContext entry = editButtons.get(id);
            ButtonWidget button = entry.button();
            if (button.isMouseOver(minecraft, mouseX, mouseY)) {
                lastClickedButton = button;
                minecraft.soundManager.method_2009("random.click", 1.0f, 1.0f);
                entry.action().onPress(button);
            }
        }

        @Override
        protected boolean isSelectedEntry(int i) {
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
            ButtonWidget button = editButtons.get(entryId).button();
            button.x = x + 10;
            button.y = y;
            button.render(minecraft, mouseX, mouseY);
        }
    }

}
