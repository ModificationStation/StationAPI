package net.modificationstation.sltest.gcapi;

import net.modificationstation.stationapi.api.config.ConfigEntry;

public class ExampleConfigCategoryTwo {

    @ConfigEntry(name = "Floating Point Value!", description = "Floats are cool.")
    public Float yayFloatingPoint = 1.0f;

    @ConfigEntry(name = "Boolean?!")
    public Boolean aBoolean = false;

    @ConfigEntry(name = "A LIST??!!")
    public String[] aList = new String[0];

    @ConfigEntry(name = "AN INTEGER LIST??!!")
    public Integer[] aIList = new Integer[0];

    @ConfigEntry(name = "A FLOAT LIST??!!")
    public Float[] aFList = new Float[0];

    @ConfigEntry(name = "A FIXED LIST?!", maxLength = 10, maxArrayLength = 3, minArrayLength = 3)
    public Integer[] aFIList = new Integer[0];
}
