package net.modificationstation.stationapi.mixin.dimension;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.world.dimension.NetherDimension;
import net.modificationstation.stationapi.api.client.level.dimension.TravelMessageProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Mixin(NetherDimension.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = TravelMessageProvider.class)
public class MixinNether implements TravelMessageProvider {

    @Unique
    private static final String
            entering = "gui." + of(MODID, "enteringNether"),
            leaving = "gui." + of(MODID, "leavingNether");

    @Override
    @Environment(EnvType.CLIENT)
    public String getEnteringTranslationKey() {
        return entering;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public String getLeavingTranslationKey() {
        return leaving;
    }
}
