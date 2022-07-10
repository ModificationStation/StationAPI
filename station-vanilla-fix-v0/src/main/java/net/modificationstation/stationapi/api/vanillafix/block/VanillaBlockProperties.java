package net.modificationstation.stationapi.api.vanillafix.block;

import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.util.StringIdentifiable;

public final class VanillaBlockProperties {

    // TODO: make this use separate blocks rather than blockstates
    public enum Color implements StringIdentifiable {
        BLACK,
        RED,
        GREEN,
        BROWN,
        BLUE,
        PURPLE,
        CYAN,
        LIGHT_GRAY,
        GRAY,
        PINK,
        LIME,
        YELLOW,
        LIGHT_BLUE,
        MAGENTA,
        ORANGE,
        WHITE;


        @Override
        public String asString() {
            return name().toLowerCase();
        }
    }
    public static final EnumProperty<Color> COLOR = EnumProperty.of("color", Color.class);
}
