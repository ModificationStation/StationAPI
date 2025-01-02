package net.modificationstation.stationapi.impl.config.screen.widget;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.config.HasDrawable;
import net.modificationstation.stationapi.api.config.HasToolTip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class IconWidget implements HasDrawable, HasToolTip {

    public int x;
    public int y;
    public int width;
    public int height;
    public String icon;

    public IconWidget(int x, int y, int width, int height, String icon) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.icon = icon;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        GL11.glBindTexture(3553, minecraft.textureManager.getTextureId(icon));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tess = Tessellator.INSTANCE;
        tess.startQuads();
        float uScale = 1f / width;
        float vScale = 1f / height;
        tess.vertex(x, y + height, 0, (float) width * uScale, (float)(height + height) * vScale);
        tess.vertex(x + width, y + height, 0, ((float)(width + width) * uScale), (float)(height + height) * vScale);
        tess.vertex(x + width, y, 0, (float)(width + width) * uScale, (float) height * vScale);
        tess.vertex(x, y, 0, (float) width * uScale, (float) height * vScale);
        tess.draw();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {

    }

    @Override
    public void setXYWH(int x, int y, int width, int height) {
        this.x = x + width + 2;
        this.y = y + height/2+1;
        this.width = 8;
        this.height = 8;
    }

    @Override
    public void tick() {

    }

    @Override
    public void keyPressed(char character, int key) {

    }

    @Override
    public void setID(int id) {

    }

    @Override
    public List<String> getTooltip() {
        return Collections.singletonList("This value is synced to the server in multiplayer");
    }

    @Override
    public int[] getXYWH() {
        return new int[]{x, y, width, height};
    }
}
