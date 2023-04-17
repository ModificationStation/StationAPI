package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.util.Util;

public interface StationBlockItemsBlock extends BlockItemToggle {

    @Override
    default BlockBase disableAutomaticBlockItemRegistration() {
        return Util.assertImpl();
    }

    @Override
    default boolean isAutomaticBlockItemRegistrationDisabled() {
        return Util.assertImpl();
    }
}
