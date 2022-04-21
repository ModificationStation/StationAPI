package net.modificationstation.stationapi.api.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
    private static final Pattern FORMATTING_CODE = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
    private static final Pattern LINE_BREAK = Pattern.compile("\\r\\n|\\v");
    private static final Pattern ENDS_WITH_LINE_BREAK = Pattern.compile("(?:\\r\\n|\\v)$");

    /**
     * {@return the length of the {@code tick} in the MM:SS format, where
     * the MM is the minutes and SS is the seconds (optionally zero-padded)}
     */
    public static String formatTicks(int ticks) {
        int i = ticks / 20;
        int j = i / 60;
        if ((i %= 60) < 10) {
            return j + ":0" + i;
        }
        return j + ":" + i;
    }

    /**
     * {@return the {@code text} with all formatting codes removed}
     * 
     * <p>A formatting code is the character {@code \u00a7} followed by
     * a numeric character or a letter A to F, K to O, or R.
     * 
     * @see Formatting#strip
     */
    public static String stripTextFormat(String text) {
        return FORMATTING_CODE.matcher(text).replaceAll("");
    }

    /**
     * {@return true if {@code text} is {@code null} or empty, false otherwise}
     */
    public static boolean isEmpty(@Nullable String text) {
        return StringUtils.isEmpty(text);
    }

    /**
     * {@return {@code text} truncated to at most {@code maxLength} characters,
     * optionally with ellipsis}
     */
    public static String truncate(String text, int maxLength, boolean addEllipsis) {
        if (text.length() <= maxLength) {
            return text;
        }
        if (addEllipsis && maxLength > 3) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text.substring(0, maxLength);
    }

    /**
     * {@return the number of linebreaks in {@code text}}
     * 
     * <p>A linebreak is either a CRLF sequence or a vertical tab (U+000B).
     */
    public static int countLines(String text) {
        if (text.isEmpty()) {
            return 0;
        }
        Matcher matcher = LINE_BREAK.matcher(text);
        int i = 1;
        while (matcher.find()) {
            ++i;
        }
        return i;
    }

    /**
     * {@return true if {@code text} ends with a linebreak, false otherwise}
     * 
     * <p>A linebreak is either a CRLF sequence or a vertical tab (U+000B).
     */
    public static boolean endsWithLineBreak(String text) {
        return ENDS_WITH_LINE_BREAK.matcher(text).find();
    }
}

