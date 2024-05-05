package net.modificationstation.stationapi.impl.config.example;

import blue.endless.jankson.Comment;
import net.modificationstation.stationapi.api.config.ConfigName;
import net.modificationstation.stationapi.api.config.MaxLength;

public class ExampleConfigCategoryTwo {

    @ConfigName("Floating Point Value!")
    @Comment("Floats are cool.")
    public Float yayFloatingPoint = 1.0f;

    @ConfigName("Boolean?!")
    public Boolean aBoolean = false;

    @ConfigName("A LIST??!!")
    public String[] aList = new String[0];

    @ConfigName("AN INTEGER LIST??!!")
    public Integer[] aIList = new Integer[0];

    @ConfigName("A FLOAT LIST??!!")
    public Float[] aFList = new Float[0];

    @ConfigName("A FIXED LIST?!")
    @MaxLength(value = 10, arrayValue = 3)
    public Integer[] aFIList = new Integer[0];
}
