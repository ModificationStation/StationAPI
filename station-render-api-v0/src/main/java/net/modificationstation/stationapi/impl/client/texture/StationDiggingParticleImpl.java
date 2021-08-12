package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.particle.Digging;
import net.minecraft.entity.ParticleBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.mixin.render.client.DiggingAccessor;
import net.modificationstation.stationapi.mixin.render.client.ParticleBaseAccessor;

public class StationDiggingParticleImpl {

    private final Digging digging;
    private final ParticleBaseAccessor particleBaseAccessor;
    public final Atlas.Texture texture;

    public StationDiggingParticleImpl(Digging digging) {
        this.digging = digging;
        particleBaseAccessor = (ParticleBaseAccessor) digging;
        texture = ((CustomAtlasProvider) ((DiggingAccessor) digging).getField_2383()).getAtlas().of(particleBaseAccessor.getField_2635()).getTexture(particleBaseAccessor.getField_2635());
    }

    public void render(float delta, float yawX, float pitchX, float yawY, float pitchY1, float pitchY2) {
        Atlas atlas = texture.getAtlas();
        Tessellator tessellator = atlas.getTessellator();
        float startU = (texture.getX() + (particleBaseAccessor.getField_2636() / 4) * texture.getWidth()) / atlas.getImage().getWidth();
        float endU = startU + 0.24975F * texture.getWidth() / atlas.getImage().getWidth();
        float startV = (texture.getY() + (particleBaseAccessor.getField_2637() / 4) * texture.getHeight()) / atlas.getImage().getHeight();
        float endV = startV + 0.24975F * texture.getHeight() / atlas.getImage().getHeight();
        float randomMultiplier = 0.1F * particleBaseAccessor.getField_2640();
        float renderX = (float)(digging.prevX + (digging.x - digging.prevX) * (double)delta - ParticleBase.field_2645);
        float renderY = (float)(digging.prevY + (digging.y - digging.prevY) * (double)delta - ParticleBase.field_2646);
        float renderZ = (float)(digging.prevZ + (digging.z - digging.prevZ) * (double)delta - ParticleBase.field_2647);
        float brightness = digging.getBrightnessAtEyes(delta);
        tessellator.colour(brightness * particleBaseAccessor.getField_2642(), brightness * particleBaseAccessor.getField_2643(), brightness * particleBaseAccessor.getField_2644());
        tessellator.vertex(renderX - yawX * randomMultiplier - pitchY1 * randomMultiplier, renderY - pitchX * randomMultiplier, renderZ - yawY * randomMultiplier - pitchY2 * randomMultiplier, startU, endV);
        tessellator.vertex(renderX - yawX * randomMultiplier + pitchY1 * randomMultiplier, renderY + pitchX * randomMultiplier, renderZ - yawY * randomMultiplier + pitchY2 * randomMultiplier, startU, startV);
        tessellator.vertex(renderX + yawX * randomMultiplier + pitchY1 * randomMultiplier, renderY + pitchX * randomMultiplier, renderZ + yawY * randomMultiplier + pitchY2 * randomMultiplier, endU, startV);
        tessellator.vertex(renderX + yawX * randomMultiplier - pitchY1 * randomMultiplier, renderY - pitchX * randomMultiplier, renderZ + yawY * randomMultiplier - pitchY2 * randomMultiplier, endU, endV);
    }
}
