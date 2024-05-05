package net.modificationstation.stationapi.impl.config.object.entry;

import com.google.common.collect.Iterables;
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
import java.util.*;

/**
 * This class is a bit of a crapshoot cause java's generic type handling is pitifully bad.
 * @param <T> The enum you want to use. Must have toString implemented. Also must be passed into the constructor.
 */
public class EnumConfigEntry<T extends Enum<?>> extends ConfigEntry<Integer> implements ConfigEntryWithButton {
    private FancyButtonWidget button;
    public final Enum<?>[] parentEnumArray;

    public EnumConfigEntry(String id, String name, String description, Field parentField, Object parentObject, boolean multiplayerSynced, Integer value, Integer defaultValue, @SuppressWarnings("rawtypes") Class parentEnum) {
        super(id, name, description, parentField, parentObject, multiplayerSynced, value, defaultValue, null);
        //noinspection unchecked Fuck off
        parentEnumArray = (Enum<?>[]) Iterables.toArray(EnumSet.allOf(parentEnum), parentEnum);
    }

    @Override
    public void init(Screen parent, TextRenderer textRenderer) {
        super.init(parent, textRenderer);
        button = new FancyButtonWidget(10, 0, 0, 0, 0, getButtonText(), CharacterUtils.getIntFromColour(new Color(255, 202, 0, 255)));
        drawableList.add(button);
        button.active = !multiplayerLoaded;
    }

    @Override
    public Integer getDrawableValue() {
        return value;
    }

    @Override
    public void setDrawableValue(Integer value) {
        this.value = value;
        if(button != null) {
            button.text = getButtonText();
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
        value++;
        if(value > parentEnumArray.length - 1) {
            value = 0;
        }
        button.text = getButtonText();
    }

    @Override
    public void reset(Object defaultValue) throws IllegalAccessException {
        value = (Integer) defaultValue;
        setDrawableValue((Integer) defaultValue);
        saveToField();
    }

    @Override
    public void saveToField() throws IllegalAccessException {
        parentField.set(parentObject, parentEnumArray[value]);
    }

    public String getButtonText() {
        return parentEnumArray[value].toString() + " (" + (value + 1) + "/" + parentEnumArray.length + ")";
    }
}
