package net.modificationstation.stationapi.api.block;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.util.Util;

public interface StationBlockItemsBlock extends BlockItemToggle {

    @Override
    default Block disableAutomaticBlockItemRegistration() {
        return Util.assertImpl();
    }

    @Override
    default boolean isAutomaticBlockItemRegistrationDisabled() {
        return Util.assertImpl();
    }
}
