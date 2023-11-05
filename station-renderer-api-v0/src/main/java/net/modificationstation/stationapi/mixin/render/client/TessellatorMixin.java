package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.impl.client.render.StationTessellatorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Tessellator.class)
class TessellatorMixin implements StationTessellator {
    @Unique
    private final StationTessellatorImpl stationapi$stationTessellatorImpl = new StationTessellatorImpl((Tessellator) (Object) this);

    @Override
    @Unique
    public void quad(BakedQuad quad, float x, float y, float z, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        stationapi$stationTessellatorImpl.quad(quad, x, y, z, colour0, colour1, colour2, colour3, normalX, normalY, normalZ, spreadUV);
    }

    @Override
    @Unique
    public void ensureBufferCapacity(int criticalCapacity) {
        stationapi$stationTessellatorImpl.ensureBufferCapacity(criticalCapacity);
    }
}
