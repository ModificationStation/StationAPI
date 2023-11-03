package net.modificationstation.stationapi.api.client.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetContextAttacher;
import net.modificationstation.stationapi.api.client.gui.widget.PressAction;

import java.util.ArrayList;
import java.util.List;

public class StationScreen extends Screen {

    protected final List<PressAction> actions = new ArrayList<>();
    @SuppressWarnings("unchecked")
    protected final ButtonWidgetContextAttacher btns = ButtonWidgetContextAttacher.toListDeconstructed(buttons, actions);

    @Override
    public void init() {
        super.init();
        buttons.clear();
        actions.clear();
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        super.buttonClicked(button);
        actions.get(button.id).onPress(button);
    }
}
