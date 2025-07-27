package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.particle;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.client.particle.BlockParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.model.block.BlockWorldModelProvider;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.world.BlockStateView;
import net.modificationstation.stationapi.mixin.arsenic.client.BlockParticleAccessor;
import net.modificationstation.stationapi.mixin.arsenic.client.ParticleAccessor;

public class ArsenicDiggingParticle {

    private final BlockParticle digging;
    private final ParticleAccessor particleBaseAccessor;
    @Getter
    private Sprite texture;

    public ArsenicDiggingParticle(BlockParticle digging) {
        this.digging = digging;
        particleBaseAccessor = (ParticleAccessor) digging;
        texture = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getSprite(((BlockParticleAccessor) digging).getField_2383().getAtlas().getTexture(particleBaseAccessor.getField_2635()).getId());
    }

    public void checkBlockCoords(int x, int y, int z) {
        Block block = ((BlockParticleAccessor) digging).getField_2383();
        if (block instanceof BlockWorldModelProvider provider)
            texture = provider.getCustomWorldModel(digging.world, x, y, z).getBaked().getSprite();
        else {
            BakedModel model = StationRenderAPI.getBakedModelManager().getBlockModels().getModel(((BlockStateView) digging.world).getBlockState(x, y, z));
            if (!model.isBuiltin())
                texture = model.getSprite();
        }
    }

    public void render(Tessellator tessellator, float delta, float yawX, float pitchX, float yawY, float pitchY1, float pitchY2) {
        float
                startU = texture.getMinU() + (particleBaseAccessor.getField_2636() / 4) * (texture.getMaxU() - texture.getMinU()),
                endU = startU + 0.24975F * (texture.getMaxU() - texture.getMinU()),
                startV = texture.getMinV() + (particleBaseAccessor.getField_2637() / 4) * (texture.getMaxV() - texture.getMinV()),
                endV = startV + 0.24975F * (texture.getMaxV() - texture.getMinV()),
                randomMultiplier = 0.1F * particleBaseAccessor.getScale(),
                renderX = (float)(digging.prevX + (digging.x - digging.prevX) * (double)delta - Particle.xOffset),
                renderY = (float)(digging.prevY + (digging.y - digging.prevY) * (double)delta - Particle.yOffset),
                renderZ = (float)(digging.prevZ + (digging.z - digging.prevZ) * (double)delta - Particle.zOffset),
                brightness = digging.getBrightnessAtEyes(delta);
        tessellator.color(brightness * particleBaseAccessor.getRed(), brightness * particleBaseAccessor.getGreen(), brightness * particleBaseAccessor.getBlue());
        tessellator.vertex(renderX - yawX * randomMultiplier - pitchY1 * randomMultiplier, renderY - pitchX * randomMultiplier, renderZ - yawY * randomMultiplier - pitchY2 * randomMultiplier, startU, endV);
        tessellator.vertex(renderX - yawX * randomMultiplier + pitchY1 * randomMultiplier, renderY + pitchX * randomMultiplier, renderZ - yawY * randomMultiplier + pitchY2 * randomMultiplier, startU, startV);
        tessellator.vertex(renderX + yawX * randomMultiplier + pitchY1 * randomMultiplier, renderY + pitchX * randomMultiplier, renderZ + yawY * randomMultiplier + pitchY2 * randomMultiplier, endU, startV);
        tessellator.vertex(renderX + yawX * randomMultiplier - pitchY1 * randomMultiplier, renderY - pitchX * randomMultiplier, renderZ + yawY * randomMultiplier - pitchY2 * randomMultiplier, endU, endV);
    }
}
