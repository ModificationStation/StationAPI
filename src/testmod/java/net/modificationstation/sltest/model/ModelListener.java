package net.modificationstation.sltest.model;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.model.ModelRegisterEvent;
import net.modificationstation.stationapi.api.client.model.CustomModel;
import net.modificationstation.stationapi.api.client.model.CustomModelRenderer;
import net.modificationstation.stationapi.api.factory.GeneralFactory;

public class ModelListener {

    @EventListener
    public void registerModels(ModelRegisterEvent event) {
        if (event.type == ModelRegisterEvent.Type.BLOCKS)
            farlandsBlockModel = GeneralFactory.INSTANCE.newInst(CustomModelRenderer.class, "/assets/sltest/stationapi/models/farlandsBlock.json", "sltest").getEntityModelBase();
    }

    public static CustomModel farlandsBlockModel;
}
