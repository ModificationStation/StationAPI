package net.modificationstation.stationapi.impl.config.screen;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationapi.api.config.CharacterUtils;
import net.modificationstation.stationapi.api.config.ConfigEntryWithButton;
import net.modificationstation.stationapi.api.config.HasDrawable;
import net.modificationstation.stationapi.impl.config.object.ConfigBase;
import net.modificationstation.stationapi.impl.config.object.ConfigCategory;
import net.modificationstation.stationapi.impl.config.object.ConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.mixin.config.client.EntryListWidgetAccessor;
import org.lwjgl.input.Mouse;

import java.util.*;

public class ScreenBuilder extends Screen {

    protected ScreenScrollList scrollList;
    protected HashMap<Integer, ConfigBase> buttonToEntry;
    protected final ConfigCategory baseCategory;
    protected int selectedIndex = -1;
    protected final Screen parent;
    protected final EntrypointContainer<Object> mod;
    protected int mouseX = -1;
    protected int mouseY = -1;
    protected List<ConfigBase> configBases = new ArrayList<>();
    protected int backButtonID;
    protected List<ButtonWidget> screenButtons = new ArrayList<>();

    public ScreenBuilder(Screen parent, EntrypointContainer<Object> mod, ConfigCategory baseCategory) {
        this.parent = parent;
        this.mod = mod;
        this.baseCategory = baseCategory;
        configBases.addAll(baseCategory.values.values());
        configBases.sort((self, other) -> {
            if (other instanceof ConfigCategory) {
                return 1;
            }
            return self instanceof ConfigCategory? -1 : self.name.compareTo(other.name);
        });
    }

    @Override
    public void init() {
        baseCategory.values.values().forEach((value) -> {
            if (value instanceof ConfigEntry<?>) {
                //noinspection rawtypes
                ConfigEntry configEntry = (ConfigEntry<?>) value;
                if (configEntry.getDrawableValue() != null) {
                    configEntry.value = configEntry.getDrawableValue();
                }
            }
        });
        buttons.clear();
        screenButtons.clear();
        this.scrollList = new ScreenScrollList();
        this.buttonToEntry = new HashMap<>();
        ButtonWidget button = new ButtonWidget(backButtonID = buttons.size(),width/2-75, height-26, 150, 20, TranslationStorage.getInstance().get("gui.done"));
        //noinspection unchecked
        buttons.add(button);
        screenButtons.add(button);
        baseCategory.values.values().forEach((value) -> {
            if (value instanceof ConfigEntry) {
                ((ConfigEntry<?>) value).init(this, textRenderer);
            }
            value.getDrawables().forEach(val -> {
                if (val instanceof ButtonWidget) {
                    val.setID(buttons.size());
                    buttonToEntry.put(buttons.size(), value);
                    //noinspection unchecked
                    buttons.add(val);
                }
            });
        });
    }

    @Override
    public void tick() {
        super.tick();
        for (ConfigBase configBase : baseCategory.values.values()) {
            if (configBase instanceof ConfigEntry) {
                configBase.getDrawables().forEach(HasDrawable::tick);
            }
        }
    }

    @Override
    protected void keyPressed(char character, int key) {
        super.keyPressed(character, key);
        for (ConfigBase configBase : baseCategory.values.values()) {
            if (configBase instanceof ConfigEntry) {
                configBase.getDrawables().forEach(val -> val.keyPressed(character, key));
            }
        }
    }

    @SuppressWarnings("CommentedOutCode") // I want to show code differences.
    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        scrollList.render(mouseX, mouseY, delta);
        // Breaks rendering of category buttons.
        //super.render(mouseX, mouseY, delta);
        //((ButtonWidget) buttons.get(backButtonID)).render(minecraft, mouseX, mouseY);
        screenButtons.forEach(button -> button.render(minecraft, mouseX, mouseY));
        textRenderer.drawWithShadow(baseCategory.name, (width/2) - (textRenderer.getWidth(baseCategory.name)/2), 4, 16777215);
        textRenderer.drawWithShadow(baseCategory.description, (width/2) - (textRenderer.getWidth(baseCategory.description)/2), 18, 8421504);
        ArrayList<HasDrawable> drawables = new ArrayList<>();
        configBases.forEach((configBase -> drawables.addAll(configBase.getDrawables())));
        if(mouseY > 32 && mouseY < height - 33) {
            List<String> tooltip = ((ScreenAccessor) this).getMouseTooltip(mouseX, mouseY, drawables);
            if (tooltip != null) {
                CharacterUtils.renderTooltip(textRenderer, tooltip, mouseX, mouseY, this);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int buttonID) {
        if(mouseY < 32 || mouseY > height - 33) {
            for (ButtonWidget button : screenButtons) {
                if (button.isMouseOver(minecraft, mouseX, mouseY)) {
                    ((ScreenAccessor) this).setSelectedButton(button);
                    minecraft.soundManager.method_2009("random.click", 1.0F, 1.0F);
                    buttonClicked(button);
                }
            }

            return;
        }
        if (buttonID != 0) { // We only want left click
            return;
        }

        for (Object buttonObj : buttons) {
            ButtonWidget button = (ButtonWidget) buttonObj;
            if (button.isMouseOver(minecraft, mouseX, mouseY)) {
                ((ScreenAccessor) this).setSelectedButton(button);
                minecraft.soundManager.method_2009("random.click", 1.0F, 1.0F);
                buttonClicked(button);
            }
        }
    }

    @Override
    public void onMouseEvent() {
        super.onMouseEvent();
        float dWheel = Mouse.getDWheel();
        if (Mouse.isButtonDown(0) && mouseY > 32 && mouseY < height - 33) {
            for (ConfigBase configBase : baseCategory.values.values()) {
                if (configBase instanceof ConfigEntry) {
                    configBase.getDrawables().forEach(val -> val.mouseClicked(mouseX, mouseY, 0));
                }
            }
        }
        else if (dWheel != 0) {
            scrollList.scroll(-(dWheel/10));
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (button.id == backButtonID) {
            saveToEntries();
            minecraft.setScreen(parent);
        }
        else if (mouseY >= 32 && mouseY <= height - 33) {
            if (buttonToEntry.get(button.id) instanceof ConfigEntryWithButton) {
                ((ConfigEntryWithButton) buttonToEntry.get(button.id)).onClick();
            }
            else if (buttonToEntry.get(button.id) instanceof ConfigCategory) {
                //noinspection deprecation
                ((Minecraft) FabricLoader.getInstance().getGameInstance()).setScreen(((ConfigCategory) buttonToEntry.get(button.id)).getConfigScreen(this, mod));
            }
        }
    }

    public void saveToEntries() {
        baseCategory.values.values().forEach((value) -> {
            if (value instanceof ConfigEntry<?>) {
                //noinspection rawtypes
                ConfigEntry configEntry = (ConfigEntry<?>) value;
                if (configEntry.isValueValid()) {
                    configEntry.value = configEntry.getDrawableValue();
                }
                else {
                    //noinspection unchecked
                    configEntry.setDrawableValue(configEntry.value);
                }
            }
        });
    }

    class ScreenScrollList extends EntryListWidget {
        public ScreenScrollList() {
            super(ScreenBuilder.this.minecraft, ScreenBuilder.this.width, ScreenBuilder.this.height, 32, ScreenBuilder.this.height - 32, 48);
            this.method_1260(false);
        }

        public void scroll(float value) {
            EntryListWidgetAccessor baseAccessor = ((EntryListWidgetAccessor) this);
            baseAccessor.setScrollAmount(baseAccessor.getScrollAmount() + value);
        }

        @Override
        protected int getEntryCount() {
            return configBases.size();
        }

        @Override
        protected void entryClicked(int entryIndex, boolean doLoad) {
            ScreenBuilder.this.selectedIndex = entryIndex;
        }

        @Override
        protected boolean isSelectedEntry(int i) {
            return i == selectedIndex;
        }

        @Override
        protected void renderBackground() {
            ScreenBuilder.this.renderBackground();
        }

        @Override
        protected void renderEntry(int itemId, int x, int y, int i1, Tessellator arg) {
            ConfigBase configBase = configBases.get(itemId);
            ScreenBuilder.this.drawTextWithShadow(ScreenBuilder.this.textRenderer, configBase.name, x + 2, y + 1, 16777215);
            configBase.getDrawables().forEach(val -> val.setXYWH(x + 2, y + 12, 212, 20));
            configBase.getDrawables().forEach(val -> val.draw(mouseX, mouseY));
            ScreenBuilder.this.drawTextWithShadow(ScreenBuilder.this.textRenderer, configBase.description, x + 2, y + 34, 8421504);
        }
    }
}
