package net.modificationstation.stationapi.mixin.network;

import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

@Mixin(NetworkHandler.class)
class NetworkHandlerMixin implements ModdedPacketHandler, ModdedPacketHandlerSetter {

    @Unique
    private Map<String, String> mods;
    @Unique
    private boolean modded = false;

    @Override
    @Unique
    public boolean isModded() {
        return modded;
    }

    @Override
    @Unique
    public void setModded(boolean value) {
        modded = value;
    }

    @Override
    @Unique
    public void setModded(Map<String, String> mods) {
        modded = true;
        this.mods = mods;
    }

    @Override
    @Unique
    public Map<String, String> getMods() {
        return mods;
    }
}
