package net.modificationstation.stationapi.impl.config.object.entry;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.config.CharacterUtils;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.api.config.HasDrawable;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.screen.widget.ExtensibleTextFieldWidget;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;

public class IntegerConfigEntryHandler extends ConfigEntryHandler<Integer> {
    private ExtensibleTextFieldWidget textbox;

    public IntegerConfigEntryHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, Integer value, Integer defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
        textValidator = str -> integerValidator(configEntry, multiplayerLoaded, str);
    }

    @Override
    public void init(Screen parent, TextRenderer textRenderer) {
        super.init(parent, textRenderer);
        textbox = new ExtensibleTextFieldWidget(textRenderer);
        textbox.setValidator(textValidator);
        textbox.setMaxLength(Math.toIntExact(configEntry.maxLength()));
        textbox.setText(value.toString());
        textbox.setEnabled(!multiplayerLoaded);
        drawableList.add(textbox);
    }

    @Override
    public Integer getDrawableValue() {
        return textbox == null? null : Integer.parseInt(textbox.getText());
    }

    @Override
    public void setDrawableValue(Integer value) {
        textbox.setText(value.toString());
    }

    @Override
    public boolean isValueValid() {
        return textValidator.apply(value.toString()) == null;
    }

    @Override
    public @NotNull List<HasDrawable> getDrawables() {
        return drawableList;
    }

    @Override
    public void reset(Object defaultValue) throws IllegalAccessException {
        value = (Integer) defaultValue;
        setDrawableValue((Integer) defaultValue);
        saveToField();
    }

    public static List<String> integerValidator(ConfigEntry configEntry, boolean multiplayerLoaded, String str) {
        if (multiplayerLoaded) {
            return Collections.singletonList("Server synced, you cannot change this value");
        }
        if (!CharacterUtils.isInteger(str)) {
            return Collections.singletonList("Value is not a whole number");
        }
        if (Integer.parseInt(str) > configEntry.maxLength()) {
            return Collections.singletonList("Value is too high");
        }
        if (Integer.parseInt(str) < configEntry.minLength()) {
            return Collections.singletonList("Value is too low");
        }
        return null;
    }
}
