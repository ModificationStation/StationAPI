package net.modificationstation.stationapi.mixin.dimension;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.world.dimension.NetherDimension;
import net.modificationstation.stationapi.api.client.world.dimension.TravelMessageProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Mixin(NetherDimension.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = TravelMessageProvider.class)
class NetherDimensionMixin implements TravelMessageProvider {
    @Unique
    private static final String
            STATIONAPI$ENTERING = "gui." + of(NAMESPACE, "enteringNether"),
            STATIONAPI$LEAVING = "gui." + of(NAMESPACE, "leavingNether");

    @Override
    @Unique
    @Environment(EnvType.CLIENT)
    public String getEnteringTranslationKey() {
        return STATIONAPI$ENTERING;
    }

    @Override
    @Unique
    @Environment(EnvType.CLIENT)
    public String getLeavingTranslationKey() {
        return STATIONAPI$LEAVING;
    }
}
