package net.modificationstation.stationapi.mixin.network;

import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NetworkHandler.class)
class NetworkHandlerMixin implements ModdedPacketHandler, ModdedPacketHandlerSetter {
    @Unique
    private boolean modded;

    @Override
    @Unique
    public boolean isModded() {
        return modded;
    }

    @Override
    @Unique
    public void setModded() {
        modded = true;
    }
}
