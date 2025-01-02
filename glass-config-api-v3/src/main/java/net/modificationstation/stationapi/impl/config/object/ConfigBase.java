package net.modificationstation.stationapi.impl.config.object;


import net.modificationstation.stationapi.api.config.HasDrawable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;

public abstract class ConfigBase {

    public final String id;

    /**
     * The name of the category. Supports colour codes. White by default.
     * Maximum length of 50.
     */
    public final String name;

    /**
     * Description of the category. Do not use colour codes. This is greyed out for legibility.
     * Maximum length of 100.
     */
    public final String description;

    public final Field parentField;

    public final Object parentObject;

    public final boolean multiplayerSynced;

    public ConfigBase(String id, String name, String description, Field parentField, Object parentObject, boolean multiplayerSynced) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentField = parentField;
        this.parentObject = parentObject;
        this.multiplayerSynced = multiplayerSynced;
    }

    @NotNull
    public abstract List<HasDrawable> getDrawables();
}
