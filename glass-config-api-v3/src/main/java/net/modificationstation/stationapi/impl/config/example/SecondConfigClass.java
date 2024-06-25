package net.modificationstation.stationapi.impl.config.example;

import net.modificationstation.stationapi.api.config.ConfigEntry;

public class SecondConfigClass {

    @ConfigEntry(name = "Test Boolean")
    public Boolean test1 = false;

    @ConfigEntry(name = "Test String")
    public String test2 = "Hmmmm";
}
