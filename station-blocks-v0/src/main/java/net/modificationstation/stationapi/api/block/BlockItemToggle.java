package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;

public interface BlockItemToggle {

    BlockBase disableAutomaticBlockItemRegistration();

    boolean isAutomaticBlockItemRegistrationDisabled();
}
