package net.modificationstation.stationapi.impl.common.mod;

import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.mod.Init;
import net.modificationstation.stationapi.api.common.event.recipe.JsonRecipeParserRegister;

public class InitImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    public static void onInitialization(Init event) {
        StationAPI.EVENT_BUS.post(new JsonRecipeParserRegister());
    }
}
