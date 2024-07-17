package net.modificationstation.stationapi.impl.config.example;

import net.modificationstation.stationapi.api.config.ConfigName;

public class SecondConfigClass {

    @ConfigName("Test Boolean")
    public Boolean test1 = false;

    @ConfigName("Test String")
    public String test2 = "Hmmmm";
}
