package net.modificationstation.sltest.gcapi;

import net.modificationstation.stationapi.api.config.ConfigCategory;
import net.modificationstation.stationapi.api.config.ConfigEntry;

/**
 * An example config category. Note the use of @ConfigCategory for defining a category.
 * Make sure to add it inside your gcapi entrypoints in your fabric.mod.json. Look at the one for this mod for an example.
 */
public class ExampleConfigCategory {

    // Same deal as before, this time it's inside a category.
    @ConfigEntry(name = "Oh No!")
    public String ohNo = "reee";

    // And functioning integer config! MUST be the class, not the primitive!
    @ConfigEntry(name = "Example Integer!")
    public Integer ohYes = 0;

    @ConfigCategory(name = "Fancy Config Category", description = "Fancy values ahead!")
    public ExampleConfigCategoryTwo secondCategory = new ExampleConfigCategoryTwo();
}
