package net.modificationstation.sltest.gcapi;

import net.modificationstation.stationapi.api.config.ConfigEntry;

public class SecondConfigClass {

    @ConfigEntry(name = "Test Boolean")
    public Boolean test1 = false;

    @ConfigEntry(name = "Test String")
    public String test2 = "Hmmmm";
}
