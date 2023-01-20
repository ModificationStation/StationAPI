package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.impl.client.render.StationTessellatorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Tessellator.class)
public class MixinTessellator implements StationTessellatorImpl.StationTessellatorAccess {

    @Unique
    private final StationTessellatorImpl stationapi$stationTessellatorImpl = new StationTessellatorImpl((Tessellator) (Object) this);

    @Override
    @Unique
    public StationTessellatorImpl stationapi$stationTessellator() {
        return stationapi$stationTessellatorImpl;
    }
}
