package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.particle.Digging;
import net.minecraft.entity.ParticleBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;

import java.util.*;

public class StationParticleManager {

    private final Map<Atlas, Stack<Digging>> diggingParticles = new HashMap<>();

    public void checkParticle(ParticleBase particle, Tessellator f, float f1, float f2, float f3, float f4, float f5, float v) {
        if (particle instanceof StationDiggingParticleProvider) {
            Atlas atlas = ((StationDiggingParticleProvider) particle).getStationDiggingParticle().getTexture().getAtlas();
            if (!diggingParticles.containsKey(atlas))
                diggingParticles.put(atlas, new Stack<>());
            diggingParticles.get(atlas).push((Digging) particle);
        } else
            particle.method_2002(f, f1, f2, f3, f4, f5, v);
    }

    public void renderAtlases(float f, float var3, float var4, float var5, float var6, float var7, Tessellator var10) {
        diggingParticles.forEach((atlas, diggings) -> {
            atlas.bindAtlas();
            var10.start();
            while (!diggings.isEmpty()) {
                diggings.pop().method_2002(var10, f, var3, var7, var4, var5, var6);
            }
            var10.draw();
        });
    }
}
