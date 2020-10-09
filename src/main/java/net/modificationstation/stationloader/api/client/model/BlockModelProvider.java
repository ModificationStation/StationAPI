package net.modificationstation.stationloader.api.client.model;

import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.impl.client.model.JsonModel;
import net.modificationstation.stationloader.impl.client.model.ModelTranslator;

public interface BlockModelProvider {

    /**
     * Override this and return the custom model made from {@link ModelTranslator#translate(JsonModel, String)}.
     *
     * The model to render in the world.
     *
     * @see ModelTranslator
     * @see ModelRegister
     */
    CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta);

    /**
     * Override this and return the custom model made from {@link ModelTranslator#translate(JsonModel, String)}.
     *
     * Model to render inside the inventory.
     *
     * @see ModelTranslator
     * @see ModelRegister
     */
    CustomModel getCustomInventoryModel(int meta);
}
