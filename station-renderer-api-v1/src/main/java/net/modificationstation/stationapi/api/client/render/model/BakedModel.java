package net.modificationstation.stationapi.api.client.render.model;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;

public interface BakedModel {

    void emitBlockQuads(BlockInputContext input, QuadEmitter output);

    void emitItemQuads(ItemInputContext input, QuadEmitter output);

    boolean useAmbientOcclusion();

    boolean hasDepth();

    boolean isSideLit();

    boolean isBuiltin();

    Sprite getSprite();

    ModelTransformation getTransformation();

    ModelOverrideList getOverrides();

    interface BlockInputContext extends InputContext {
        boolean isFluidModel();

        BlockState blockState();

        BlockPos pos();
    }

    interface ItemInputContext extends InputContext {
        ItemStack itemStack();
    }
}
