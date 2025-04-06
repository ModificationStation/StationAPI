package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.impl.client.render.StationTessellatorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Tessellator.class)
abstract
class TessellatorMixin implements StationTessellator, VertexConsumer {

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

    @Override @Shadow
    public abstract void vertex(double x, double y, double z);

    @Override
    public void vertex(float x, float y, float z) {
        vertex((double) x, (double) y, (double) z);
    }

    @Override @Shadow
    public abstract void color(int red, int green, int blue, int alpha);

    @Override @Shadow
    public abstract void texture(double u, double v);

    @Override @Shadow
    public abstract void normal(float x, float y, float z);
}
