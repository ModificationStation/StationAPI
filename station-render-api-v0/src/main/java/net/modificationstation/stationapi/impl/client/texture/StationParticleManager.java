package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.ParticleBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

import java.util.*;

public class StationParticleManager {

    public final Set<Atlas> activeAtlases = new HashSet<>();

    public void checkParticle(ParticleBase particle) {
        if (particle instanceof StationDiggingParticleProvider) {
            Atlas atlas = ((StationDiggingParticleProvider) particle).getStationDiggingParticle().texture.getAtlas();
            Tessellator tessellator = atlas.getTessellator();
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                activeAtlases.add(atlas);
                tessellator.start();
            }
        }
    }

    public void renderAtlases() {
        if (!activeAtlases.isEmpty()) {
            activeAtlases.forEach(atlas -> {
                atlas.bindAtlas();
                atlas.getTessellator().draw();
            });
            activeAtlases.clear();
        }
    }
}
