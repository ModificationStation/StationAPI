package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.config.ConfigEntryWithButton;
import net.modificationstation.stationapi.api.config.HasDrawable;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.screen.BaseListScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.widget.FancyButtonWidget;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;

public abstract class BaseListConfigEntryHandler<T> extends ConfigEntryHandler<T[]> implements ConfigEntryWithButton {
    @Environment(EnvType.CLIENT)
    private BaseListScreenBuilder<T> listScreen;
    @Environment(EnvType.CLIENT)
    private FancyButtonWidget button;

    public BaseListConfigEntryHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, T[] value, T[] defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
    }

    @Override
    public void init(Screen parent, TextRenderer textRenderer) {
        super.init(parent, textRenderer);
        button = new FancyButtonWidget(10, 0, 0, 0, 0, "Open List... (" + value.length + " values)");
        drawableList.add(button);
        listScreen = createListScreen();
        button.active = !multiplayerLoaded;
    }

    @Environment(EnvType.CLIENT)
    public abstract BaseListScreenBuilder<T> createListScreen();

    public abstract T strToVal(String str);

    @Override
    public T[] getDrawableValue() {
        if (listScreen == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        listScreen.textFieldWidgets.forEach((val) -> {
            if (val.isValueValid()) {
                list.add(strToVal(val.getText()));
            }
        });
        //noinspection unchecked This class should only ever be used by arrays.
        return (T[]) list.toArray(new Object[0]);
    }

    @Override
    public boolean isValueValid() {
        return value.length < configEntry.maxArrayLength() && value.length > configEntry.minArrayLength();
    }

    @Override
    public void setDrawableValue(T[] value) {
        listScreen.setValues(value);
    }

    @Override
    public @NotNull List<HasDrawable> getDrawables() {
        return drawableList;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onClick() {
        //noinspection deprecation
        ((net.minecraft.client.Minecraft) FabricLoader.getInstance().getGameInstance()).setScreen(listScreen);
    }

    @Override
    public void reset(Object defaultValue) throws IllegalAccessException { // !!OVERRIDE THIS AND DO A DEEP CLONE IF YOU'RE USING SOMETHING THAT ISN'T A PRIMITIVE!!
        //noinspection unchecked
        value = ((T[]) defaultValue).clone();
        saveToField();
    }
}

