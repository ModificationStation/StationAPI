package net.modificationstation.stationapi.impl.config.screen;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.modificationstation.stationapi.api.config.ConfigRoot;
import net.modificationstation.stationapi.impl.config.ConfigRootEntry;
import net.modificationstation.stationapi.impl.config.EventStorage;
import net.modificationstation.stationapi.impl.config.GCCore;
import net.modificationstation.stationapi.impl.config.object.ConfigCategoryHandler;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.*;
import java.util.function.*;

public class RootScreenBuilder extends ScreenBuilder {

    private final ArrayList<ConfigRootEntry> allRoots = new ArrayList<>();
    private final List<Integer> switchButtons = new ArrayList<>();
    public int currentIndex = 1; // Arrays start at 1 :fatlaugh:

    public RootScreenBuilder(Screen parent, ModContainer mod, ConfigCategoryHandler baseCategory) {
        super(parent, mod, baseCategory);
        //noinspection deprecation
        GCCore.MOD_CONFIGS.forEach((key, value) -> {
            if (key.namespace.toString().equals(mod.getMetadata().getId())) {
                allRoots.add(value);
            }
        });

//        allRoots.sort(Collections.reverseOrder(Comparator.comparingInt(entry -> entry.configRoot().index())));
        allRoots.sort(Comparator.comparingInt(entry -> entry.configRoot().index()));
    }

    @Override
    public void init() {
        super.init();
        switchButtons.clear();
        if(allRoots.size() > 1) {
            int prevRoot = currentIndex - 1;
            if (prevRoot < 0) {
                prevRoot = allRoots.size()-1;
            }
            ButtonWidget button = new ButtonWidget(buttons.size(), 2, 0, 160, 20, "< " + allRoots.get(prevRoot).configRoot().visibleName());
            //noinspection unchecked
            buttons.add(button);
            screenButtons.add(button);
            switchButtons.add(button.id);

            int nextRoot = currentIndex + 1;
            if (nextRoot > allRoots.size()-1) {
                nextRoot = 0;
            }
            button = new ButtonWidget(buttons.size(), width - 162, 0, 160, 20,  allRoots.get(nextRoot).configRoot().visibleName() + " >");
            //noinspection unchecked
            buttons.add(button);
            screenButtons.add(button);
            switchButtons.add(button.id);
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        super.buttonClicked(button);
        if (button.id == backButtonID) {
            //noinspection deprecation Intentional use of GCCore internals.
            GCCore.saveConfig(mod, baseCategory, EventStorage.EventSource.USER_SAVE);
        }
        if (switchButtons.contains(button.id)) {
            int index = switchButtons.get(0) == button.id? -1 : 1;
            index += currentIndex;
            if (index > allRoots.size()-1) {
                index = 0;
            }
            else if (index < 0) {
                index = allRoots.size()-1;
            }
            RootScreenBuilder builder = (RootScreenBuilder) allRoots.get(index).configCategoryHandler().getConfigScreen(parent, mod);
            builder.currentIndex = index;
            //noinspection deprecation
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).setScreen(builder);
        }
    }
}
