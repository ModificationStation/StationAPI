package net.modificationstation.stationapi.api.client.option;

import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.client.option.StationKeyBindingsManager;

public class StationKeyBinding extends KeyBinding {
    public final Identifier id;

    public StationKeyBinding(Identifier id, Identifier translationKey, int code) {
        super("key." + translationKey, code);
        this.id = id;
        StationKeyBindingsManager.registerKeyBinding(this);
    }
}
