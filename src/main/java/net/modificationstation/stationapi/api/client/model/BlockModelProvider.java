package net.modificationstation.stationapi.api.client.model;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.event.model.ModelRegisterEvent;
import net.modificationstation.stationapi.impl.client.model.JsonModel;
import net.modificationstation.stationapi.impl.client.model.ModelTranslator;

public interface BlockModelProvider {

    /**
     * Override this and return the custom model made from {@link ModelTranslator#translate(JsonModel, String)}.
     * <p>
     * The model to render in the world.
     *
     * @see ModelTranslator
     * @see ModelRegisterEvent
     */
    CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta);

    /**
     * Override this and return the custom model made from {@link ModelTranslator#translate(JsonModel, String)}.
     * <p>
     * Model to render inside the inventory.
     *
     * @see ModelTranslator
     * @see ModelRegisterEvent
     */
    CustomModel getCustomInventoryModel(int meta);
}
