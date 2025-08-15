package net.modificationstation.stationapi.mixin.network;

import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.network.ModdedPacketHandlerSetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

@Mixin(NetworkHandler.class)
class NetworkHandlerMixin implements ModdedPacketHandler, ModdedPacketHandlerSetter {

    private Map<String, String> mods;

    @Override
    @Unique
    public boolean isModded() {
        return mods != null;
    }

    @Override
    @Unique
    public void setModded(Map<String, String> mods) {
        this.mods = mods;
    }

    public Map<String, String> getMods() {
        return mods;
    }
}
