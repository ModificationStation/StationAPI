package net.modificationstation.stationapi.api.client.render.model;

import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;

public final class VanillaBakedModel implements BakedModel {

    @Override
    public void emitBlockQuads(BlockInputContext input, QuadEmitter output) {}

    @Override
    public void emitItemQuads(ItemInputContext input, QuadEmitter output) {}

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public Sprite getSprite() {
        return StationRenderAPI.getBakedModelManager().getMissingModel().getSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelTransformation.NONE;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }
}
