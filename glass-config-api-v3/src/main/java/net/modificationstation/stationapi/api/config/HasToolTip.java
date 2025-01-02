package net.modificationstation.stationapi.api.config;

import java.util.*;

public interface HasToolTip {

    /**
     * The tooltip to show, can be multiline by adding another element onto the list.
     * @return a List of colour-enabled strings to use as a tooltip.
     */
    List<String> getTooltip();

    /**
     * The position and size of your element. Required.
     * @return a 4 length array of ints, the X position, Y position, width and height of your element.
     */
    int[] getXYWH();
}
