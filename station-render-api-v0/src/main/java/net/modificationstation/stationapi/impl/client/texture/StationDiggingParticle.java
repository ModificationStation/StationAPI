package net.modificationstation.stationapi.impl.client.texture;

import lombok.Getter;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.particle.Digging;
import net.minecraft.entity.ParticleBase;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.model.block.BlockWorldModelProvider;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.impl.block.BlockStateProvider;
import net.modificationstation.stationapi.mixin.render.client.DiggingAccessor;
import net.modificationstation.stationapi.mixin.render.client.ParticleBaseAccessor;

public class StationDiggingParticle {

    private final Digging digging;
    private final ParticleBaseAccessor particleBaseAccessor;
    @Getter
    private Sprite texture;

    public StationDiggingParticle(Digging digging) {
        this.digging = digging;
        particleBaseAccessor = (ParticleBaseAccessor) digging;
        texture = StationRenderAPI.BAKED_MODEL_MANAGER.getAtlas(Atlases.GAME_ATLAS_TEXTURE).getSprite(((CustomAtlasProvider) ((DiggingAccessor) digging).getField_2383()).getAtlas().getTexture(particleBaseAccessor.getField_2635()).getId());
    }

    public void checkBlockCoords(int x, int y, int z) {
        BlockBase block = ((DiggingAccessor) digging).getField_2383();
        if (block instanceof BlockWorldModelProvider provider)
            texture = provider.getCustomWorldModel(digging.level, x, y, z).getBaked().getSprite();
        else {
            BakedModel model = StationRenderAPI.BAKED_MODEL_MANAGER.getBlockModels().getModel(((BlockStateProvider) digging.level).getBlockState(x, y, z));
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
                randomMultiplier = 0.1F * particleBaseAccessor.getField_2640(),
                renderX = (float)(digging.prevX + (digging.x - digging.prevX) * (double)delta - ParticleBase.field_2645),
                renderY = (float)(digging.prevY + (digging.y - digging.prevY) * (double)delta - ParticleBase.field_2646),
                renderZ = (float)(digging.prevZ + (digging.z - digging.prevZ) * (double)delta - ParticleBase.field_2647),
                brightness = digging.getBrightnessAtEyes(delta);
        tessellator.colour(brightness * particleBaseAccessor.getField_2642(), brightness * particleBaseAccessor.getField_2643(), brightness * particleBaseAccessor.getField_2644());
        tessellator.vertex(renderX - yawX * randomMultiplier - pitchY1 * randomMultiplier, renderY - pitchX * randomMultiplier, renderZ - yawY * randomMultiplier - pitchY2 * randomMultiplier, startU, endV);
        tessellator.vertex(renderX - yawX * randomMultiplier + pitchY1 * randomMultiplier, renderY + pitchX * randomMultiplier, renderZ - yawY * randomMultiplier + pitchY2 * randomMultiplier, startU, startV);
        tessellator.vertex(renderX + yawX * randomMultiplier + pitchY1 * randomMultiplier, renderY + pitchX * randomMultiplier, renderZ + yawY * randomMultiplier + pitchY2 * randomMultiplier, endU, startV);
        tessellator.vertex(renderX + yawX * randomMultiplier - pitchY1 * randomMultiplier, renderY - pitchX * randomMultiplier, renderZ + yawY * randomMultiplier - pitchY2 * randomMultiplier, endU, endV);
    }
}
