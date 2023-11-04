package net.modificationstation.stationapi.api.block;

import net.minecraft.block.Block;

public interface BlockItemToggle {

    Block disableAutoItemRegistration();

    boolean isAutoItemRegistrationDisabled();
}
