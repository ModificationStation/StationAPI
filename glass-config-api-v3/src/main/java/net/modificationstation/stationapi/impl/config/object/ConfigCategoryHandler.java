package net.modificationstation.stationapi.impl.config.object;

import com.google.common.collect.Multimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.config.HasDrawable;
import net.modificationstation.stationapi.impl.config.screen.RootScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.ScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.widget.FancyButtonWidget;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;

public class ConfigCategoryHandler extends ConfigHandlerBase {

    public final boolean isRoot;
    public Multimap<Class<?>, ConfigHandlerBase> values;

    private List<HasDrawable> button;

    public ConfigCategoryHandler(String id, String name, String description, Field parentField, Object parentObject, boolean multiplayerSynced, Multimap<Class<?>, ConfigHandlerBase> values, boolean isRoot) {
        super(id, name, description, parentField, parentObject, multiplayerSynced);
        this.values = values;
        this.isRoot = isRoot;
    }

    /**
     * The ScreenBuilder for this category. Can only have config entries.
     * @return ScreenBuilder
     */
    @Environment(EnvType.CLIENT)
    public @NotNull ScreenBuilder getConfigScreen(Screen parent, ModContainer mod) {
        return isRoot ? new RootScreenBuilder(parent, mod, this) : new ScreenBuilder(parent, mod, this);
    }

    @Override
    public @NotNull List<HasDrawable> getDrawables() {
        if (button == null) {
            button = Collections.singletonList(new FancyButtonWidget(0, 0, 0, "Open"));
        }
        return button;
    }
}
