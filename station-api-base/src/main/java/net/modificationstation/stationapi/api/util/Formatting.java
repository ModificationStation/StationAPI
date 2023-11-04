package net.modificationstation.stationapi.api.util;

public enum Formatting {
    @API
    BLACK('0'),
    @API
    DARK_BLUE('1'),
    @API
    DARK_GREEN('2'),
    @API
    DARK_AQUA('3'),
    @API
    DARK_RED('4'),
    @API
    DARK_PURPLE('5'),
    @API
    GOLD('6'),
    @API
    GRAY('7'),
    @API
    DARK_GRAY('8'),
    @API
    BLUE('9'),
    @API
    GREEN('a'),
    @API
    AQUA('b'),
    @API
    RED('c'),
    @API
    LIGHT_PURPLE('d'),
    @API
    YELLOW('e'),
    @API
    WHITE('f');

    @API
    public static final char FORMATTING_CODE_PREFIX = '§';

    private final String stringValue;

    Formatting(char code) {
        stringValue = FORMATTING_CODE_PREFIX + "" + code;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
