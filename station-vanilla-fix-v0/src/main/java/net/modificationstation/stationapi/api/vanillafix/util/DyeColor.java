package net.modificationstation.stationapi.api.vanillafix.util;

import net.minecraft.block.WoolBlock;
import net.minecraft.item.DyeItem;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;

public enum DyeColor implements StringIdentifiable {
    WHITE(0, "white"),
    ORANGE(1, "orange"),
    MAGENTA(2, "magenta"),
    LIGHT_BLUE(3, "light_blue"),
    YELLOW(4, "yellow"),
    LIME(5, "lime"),
    PINK(6, "pink"),
    GRAY(7, "gray"),
    LIGHT_GRAY(8, "light_gray"),
    CYAN(9, "cyan"),
    PURPLE(10, "purple"),
    BLUE(11, "blue"),
    BROWN(12, "brown"),
    GREEN(13, "green"),
    RED(14, "red"),
    BLACK(15, "black");

    private static final DyeColor[] VALUES;
    private final int id;
    private final String name;
    private final float[] colorComponents;

    DyeColor(int id, String name) {
        this.id = id;
        this.name = name;
        int color = DyeItem.colors[WoolBlock.getBlockMeta(id)];
        int j = (color & 0xFF0000) >> 16;
        int k = (color & 0xFF00) >> 8;
        int l = (color & 0xFF);
        this.colorComponents = new float[]{(float)j / 255.0f, (float)k / 255.0f, (float)l / 255.0f};
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Returns the red, blue and green components of this dye color.
     *
     * @return an array composed of the red, blue and green floats
     */
    public float[] getColorComponents() {
        return this.colorComponents;
    }

    public static DyeColor byId(int id) {
        if (id < 0 || id >= VALUES.length) {
            id = 0;
        }
        return VALUES[id];
    }

    @Nullable
    @Contract(value="_,!null->!null;_,null->_")
    public static DyeColor byName(String name, @Nullable DyeColor defaultColor) {
        for (DyeColor dyeColor : DyeColor.values()) {
            if (!dyeColor.name.equals(name)) continue;
            return dyeColor;
        }
        return defaultColor;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    static {
        VALUES = Arrays.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).toArray(DyeColor[]::new);
    }
}
