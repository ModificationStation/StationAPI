package net.modificationstation.stationapi.mixin.render.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.DelegatingTessellator;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.block.StationRendererBlockRenderManager;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(BlockRenderManager.class)
abstract class BlockRenderManagerMixin implements StationRendererBlockRenderManager {
    @Unique
    private final MutableBlockPos stationapi_pos = new MutableBlockPos(0, 0, 0);
    @Unique
    private final Random stationapi_random = new Random();
    @Unique
    private final DelegatingTessellator delegator = new DelegatingTessellator();

    @Shadow public abstract void renderWithoutCulling(Block arg, int i, int j, int k);

    @Shadow private BlockView blockView;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    @Unique
    public void renderAllSides(VertexConsumer consumer, BlockState state, int x, int y, int z) {
        delegator.setDelegate(consumer);
        if (StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state) instanceof VanillaBakedModel)
            renderWithoutCulling(state.getBlock(), x, y, z);
        else
            Renderer.get().bakedModelRenderer().renderBlock(consumer, state, stationapi_pos.set(x, y, z), blockView, false, stationapi_random);
        delegator.setDelegate(Tessellator.INSTANCE);
    }

    @Override
    @Unique
    public void setVertexConsumer(VertexConsumer vertexConsumer) {
        this.delegator.setDelegate(vertexConsumer);
    }

    @Override
    public VertexConsumer getVertexConsumer() {
        return this.delegator;
    }

    @ModifyExpressionValue(method = "*", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/Tessellator;INSTANCE:Lnet/minecraft/client/render/Tessellator;"))
    private Tessellator modifyTessellator(Tessellator value) {
        return delegator;
    }
}
