package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.api.client.render.block.StationRendererBlockRenderManager;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import net.modificationstation.stationapi.api.util.maths.MutableBlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(BlockRenderer.class)
abstract class BlockRenderManagerMixin implements StationRendererBlockRenderManager {
    @Unique
    private final MutableBlockPos stationapi_pos = new MutableBlockPos(0, 0, 0);
    @Unique
    private final Random stationapi_random = new Random();

    @Shadow public abstract void renderAllSides(BlockBase arg, int i, int j, int k);

    @Shadow private BlockView blockView;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    @Unique
    public void renderAllSides(BlockState state, int x, int y, int z) {
        if (StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state) instanceof VanillaBakedModel)
            renderAllSides(state.getBlock(), x, y, z);
        else if (RendererAccess.INSTANCE.hasRenderer())
            RendererAccess.INSTANCE.getRenderer().bakedModelRenderer().renderBlock(state, stationapi_pos.set(x, y, z), blockView, false, stationapi_random);
    }
}
