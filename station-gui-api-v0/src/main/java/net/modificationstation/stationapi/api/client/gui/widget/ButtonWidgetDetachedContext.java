package net.modificationstation.stationapi.api.client.gui.widget;

public record ButtonWidgetDetachedContext(ButtonWidgetFactory buttonFactory, PressAction action) {

    public ButtonWidgetAttachedContext attach(int id) {
        return new ButtonWidgetAttachedContext(buttonFactory.newButton(id), action);
    }
}
