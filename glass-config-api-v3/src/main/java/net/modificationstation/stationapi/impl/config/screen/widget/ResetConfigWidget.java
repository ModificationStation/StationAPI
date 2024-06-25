package net.modificationstation.stationapi.impl.config.screen.widget;

import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;

import java.util.*;

public class ResetConfigWidget extends IconWidget {
    private final ConfigEntryHandler<?> configEntry;

    public ResetConfigWidget(int x, int y, int width, int height, ConfigEntryHandler<?> configEntry) {
        super(x, y, width, height, "/assets/gcapi/reset.png");
        this.configEntry = configEntry;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
            try {
                if(!configEntry.multiplayerLoaded) {
                    configEntry.reset(configEntry.defaultValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setXYWH(int x, int y, int width, int height) {
        super.setXYWH(x, y, width, height);
        this.y -= 10;
    }

    @Override
    public List<String> getTooltip() {
        if(configEntry.multiplayerLoaded) {
            return new ArrayList<>();
        }
        return Collections.singletonList("Reset this config to default.");
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if(!configEntry.multiplayerLoaded) {
            super.draw(mouseX, mouseY);
        }
    }
}
