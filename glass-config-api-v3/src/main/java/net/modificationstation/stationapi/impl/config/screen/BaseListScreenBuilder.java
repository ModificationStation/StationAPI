package net.modificationstation.stationapi.impl.config.screen;

import net.minecraft.class_35;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.config.CharacterUtils;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.screen.widget.ExtensibleTextFieldWidget;
import net.modificationstation.stationapi.impl.config.screen.widget.TexturedButtonWidget;
import net.modificationstation.stationapi.mixin.config.client.EntryListWidgetAccessor;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

public abstract class BaseListScreenBuilder<T> extends Screen {

    protected ScreenScrollList scrollList;
    protected final Screen parent;
    protected int mouseX = -1;
    protected int mouseY = -1;
    protected ConfigEntryHandler<T[]> configEntry;
    public final List<ExtensibleTextFieldWidget> textFieldWidgets = new ArrayList<>();
    protected Function<String, List<String>> validator;
    protected final ConfigEntry configAnnotation;
    private boolean isInUse = false;

    protected BaseListScreenBuilder(Screen parent, ConfigEntry configAnnotation, ConfigEntryHandler<T[]> configEntry, Function<String, List<String>> validator) {
        this.parent = parent;
        this.configAnnotation = configAnnotation;
        this.configEntry = configEntry;
        this.validator = validator;
    }

    public void setValues(List<T> list) {
        textFieldWidgets.clear();
        list.forEach((value) -> {
            ExtensibleTextFieldWidget textbox = new ExtensibleTextFieldWidget(textRenderer);
            textbox.setValidator(validator);
            textbox.setMaxLength(Math.toIntExact(configAnnotation.maxLength())); // This helper throws an exception if the number is too high for an int. Handy!
            textbox.setText(String.valueOf(value));
            textFieldWidgets.add(textbox);
        });
    }

    public void setValues(T[] list) {
        setValues(Arrays.asList(list));
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        this.field_157 = new class_35(minecraft);
        this.minecraft = minecraft;
        this.textRenderer = minecraft.textRenderer;
        this.width = width;
        this.height = height;
        init();
    }

    @Override
    public void init() {
        if (isInUse) {
            scrollList = new ScreenScrollList();
            ButtonWidget button = ((ButtonWidget)buttons.get(0));
            button.x = width/2-75;
            button.y = height-26;
            button = ((ButtonWidget)buttons.get(1));
            button.x = ((width/3)*2)-75;
            button.y = height-48;
            return;
        }
        setValues(configEntry.value);
        buttons.clear();
        this.scrollList = new ScreenScrollList();
        //noinspection unchecked
        buttons.add(new ButtonWidget(0,width/2-75, height-26, 150, 20, TranslationStorage.getInstance().get("gui.cancel")));
        //noinspection unchecked
        buttons.add(new TexturedButtonWidget(1,((width/3)*2)-75, height-48, 20, 20, 0, 0, "assets/gcapi/add_button.png", 32, 64, "Add a new entry at the end"));
        AtomicInteger id = new AtomicInteger(1);
        textFieldWidgets.forEach((te) -> {
            //noinspection unchecked
            buttons.add(new TexturedButtonWidget(id.incrementAndGet(),0, 0, 20, 20, 0, 0, "assets/gcapi/remove_button.png", 32, 64, "Remove the entry on this line"));
        });
        isInUse = true;
    }

    @Override
    public void tick() {
        super.tick();
        for (ExtensibleTextFieldWidget configBase : textFieldWidgets) {
            configBase.tick();
        }
    }

    @Override
    protected void keyPressed(char character, int key) {
        super.keyPressed(character, key);
        for (ExtensibleTextFieldWidget configBase : textFieldWidgets) {
            configBase.keyPressed(character, key);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        scrollList.render(mouseX, mouseY, delta);
        // Breaks rendering of category buttons.
        //super.render(mouseX, mouseY, delta);
        ((ButtonWidget) buttons.get(0)).render(minecraft, mouseX, mouseY);
        ((ButtonWidget) buttons.get(1)).render(minecraft, mouseX, mouseY);
        textRenderer.drawWithShadow(configEntry.name, (width / 2) - (textRenderer.getWidth(configEntry.name) / 2), 4, 16777215);
        textRenderer.drawWithShadow(configEntry.description, (width / 2) - (textRenderer.getWidth(configEntry.description) / 2), 18, 8421504);

        ConfigEntry maxLength = configEntry.parentField.getAnnotation(ConfigEntry.class);
        if (textFieldWidgets.size() < maxLength.minArrayLength() || textFieldWidgets.size() > maxLength.maxArrayLength()) {
            String text = "Array is not the right size! (" + textFieldWidgets.size() + " outside of " + maxLength.minArrayLength() + " / " + maxLength.maxArrayLength() + ")";
            textRenderer.drawWithShadow(text, (width / 2) - (textRenderer.getWidth(text) / 2), 34, CharacterUtils.getIntFromColour(Color.RED));
        }

        List<String> tooltip = ((ScreenAccessor) this).getMouseTooltip(mouseX, mouseY, textFieldWidgets);
        if (tooltip != null) {
            CharacterUtils.renderTooltip(textRenderer, tooltip, mouseX, mouseY, this);
        }
    }

    @Override
    public void onMouseEvent() {
        super.onMouseEvent();
        float dWheel = Mouse.getDWheel();
        if (Mouse.isButtonDown(0)) {
            for (ExtensibleTextFieldWidget configBase : textFieldWidgets) {
                    configBase.mouseClicked(mouseX, mouseY, 0);
            }
        }
        else if (dWheel != 0) {
            scrollList.scroll(-(dWheel/10));
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (button.id == 0) {
            isInUse = false;
            minecraft.setScreen(parent);
        }
        else if (button.id == 1) {
            ExtensibleTextFieldWidget textbox = new ExtensibleTextFieldWidget(textRenderer);
            textbox.setValidator(validator);
            textbox.setText("");
            textFieldWidgets.add(textbox);
            //noinspection unchecked
            buttons.add(new TexturedButtonWidget(buttons.size(),0, 0, 20, 20, 0, 0, "assets/gcapi/remove_button.png", 32, 64, "Remove the entry on this line"));
        }
        else if (button.id > 1) {
            textFieldWidgets.remove(button.id-2);
            buttons.remove(button.id);
            for (int i = 1; i<buttons.size(); i++) {
                ((ButtonWidget) buttons.get(i)).id = i;
            }
        }
    }

    abstract T convertStringToValue(String value);

    @Override
    public void removed() {
        if (isInUse) {
            return;
        }
        List<T> list = new ArrayList<>();
        textFieldWidgets.forEach((value) -> {
            if (value.isValueValid()) {
                list.add(convertStringToValue(value.getText()));
            }
        });
        //noinspection unchecked
        configEntry.value = (T[]) list.toArray();
        super.removed();
    }

    class ScreenScrollList extends EntryListWidget {
        public ScreenScrollList() {
            super(BaseListScreenBuilder.this.minecraft, BaseListScreenBuilder.this.width, BaseListScreenBuilder.this.height, 32, BaseListScreenBuilder.this.height - 64, 24);

        }

        public void scroll(float value) {
            EntryListWidgetAccessor baseAccessor = ((EntryListWidgetAccessor) this);
            baseAccessor.setScrollAmount(baseAccessor.getScrollAmount() + value);
        }

        @Override
        protected int getEntryCount() {
            return textFieldWidgets.size();
        }

        @Override
        protected void entryClicked(int entryIndex, boolean doLoad) {
        }

        @Override
        protected boolean isSelectedEntry(int i) {
            return false;
        }

        @Override
        protected void renderBackground() {
            BaseListScreenBuilder.this.renderBackground();
        }

        @Override
        protected void renderEntry(int itemId, int x, int y, int i1, net.minecraft.client.render.Tessellator arg) {
            if (itemId+2 >= buttons.size()) {
                return;
            }
            ExtensibleTextFieldWidget configBase = textFieldWidgets.get(itemId);
            BaseListScreenBuilder.this.drawTextWithShadow(textRenderer, String.valueOf(itemId), x - 2 - textRenderer.getWidth(String.valueOf(itemId)), y + 1, 16777215);
            ((TexturedButtonWidget) buttons.get(itemId+2)).setPos(x + 214 + 34, y+2);
            ((TexturedButtonWidget) buttons.get(itemId+2)).render(minecraft, mouseX, mouseY);
            configBase.setXYWH(x + 2, y + 1, 212, 20);
            configBase.draw(mouseX, mouseY);
        }
    }
}
