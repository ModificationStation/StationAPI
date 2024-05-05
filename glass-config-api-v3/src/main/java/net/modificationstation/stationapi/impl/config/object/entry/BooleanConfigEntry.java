package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.config.CharacterUtils;
import net.modificationstation.stationapi.api.config.ConfigEntryWithButton;
import net.modificationstation.stationapi.api.config.HasDrawable;
import net.modificationstation.stationapi.impl.config.object.ConfigEntry;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.impl.config.screen.widget.FancyButtonWidget;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.lang.reflect.*;
import java.util.List;

public class BooleanConfigEntry extends ConfigEntry<Boolean> implements ConfigEntryWithButton {
    private FancyButtonWidget button;

    public BooleanConfigEntry(String id, String name, String description, Field parentField, Object parentObject, boolean multiplayerSynced, Boolean value, Boolean defaultValue) {
        super(id, name, description, parentField, parentObject, multiplayerSynced, value, defaultValue, null);
    }

    @Override
    public void init(Screen parent, TextRenderer textRenderer) {
        super.init(parent, textRenderer);
        button = new FancyButtonWidget(10, 0, 0, 0, 0, value.toString(), CharacterUtils.getIntFromColour(new Color(255, 202, 0, 255)));
        drawableList.add(button);
        button.active = !multiplayerLoaded;
    }

    @Override
    public Boolean getDrawableValue() {
        return value;
    }

    @Override
    public void setDrawableValue(Boolean value) {
        this.value = value;
        if(button != null) {
            button.text = value.toString();
        }
    }

    @Override
    public boolean isValueValid() {
        return true;
    }

    @Override
    public @NotNull List<HasDrawable> getDrawables() {
        return drawableList;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onClick() {
        value = !value;
        button.text = value.toString();
    }

    @Override
    public void reset(Object defaultValue) throws IllegalAccessException {
        value = (Boolean) defaultValue;
        setDrawableValue((Boolean) defaultValue);
        saveToField();
    }
}
