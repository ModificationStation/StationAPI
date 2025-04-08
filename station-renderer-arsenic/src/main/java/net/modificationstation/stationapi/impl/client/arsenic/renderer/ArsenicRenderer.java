package net.modificationstation.stationapi.impl.client.arsenic.renderer;

import com.google.common.base.Suppliers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.StateManager;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.material.MaterialFinder;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.client.render.mesh.MutableMesh;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.render.model.SpriteFinder;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.BakedModelRendererImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.SimpleBlockRenderContext;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.StateManagerImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.material.MaterialFinderImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.MutableMeshImpl;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArsenicRenderer implements Renderer {
    public static final ArsenicRenderer INSTANCE = new ArsenicRenderer();

    public static final RenderMaterial STANDARD_MATERIAL = INSTANCE.materialFinder().find();

    static {
        INSTANCE.registerMaterial(RenderMaterial.STANDARD_ID, STANDARD_MATERIAL);
    }

    private final Map<Identifier, RenderMaterial> materialMap = new HashMap<>();

    @Override
    public MutableMesh mutableMesh() {
        return new MutableMeshImpl();
    }

    @Override
    public MaterialFinder materialFinder() {
        return new MaterialFinderImpl();
    }

    @Override
    public RenderMaterial materialById(Identifier id) {
        return materialMap.get(id);
    }

    @Override
    public boolean registerMaterial(Identifier id, RenderMaterial material) {
        if (materialMap.containsKey(id)) return false;

        // cast to prevent acceptance of impostor implementations
        materialMap.put(id, material);
        return true;
    }

    @Override
    public SpriteFinder getSpriteFinder(SpriteAtlasTexture atlas) {
        return SpriteFinderImpl.get(atlas);
    }

    @Override
    public BakedModelRenderer bakedModelRenderer() {
        return bakedModelRenderer.get();
    }

    @Override
    public StateManager stateManager() {
        return stateManager.get();
    }

    @Override
    public void render(BakedModelRenderer modelRenderer, BlockView blockView, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrices, IntFunction<VertexConsumer> vertexConsumers, boolean cull, long seed) {
        TerrainLikeRenderContext.POOL.get().bufferModel(blockView, model, state, pos, matrices, vertexConsumers, cull, seed);
    }

    @Override
    public void render(MatrixStack matrices, IntFunction<VertexConsumer> vertexConsumers, BakedModel model, float red, float green, float blue, int light, int overlay, BlockView blockView, BlockPos pos, BlockState state) {
        SimpleBlockRenderContext.POOL.get().bufferModel(matrices, vertexConsumers, model, red, green, blue, light, blockView, pos, state);
    }

    @Override
    public void renderBlockAsEntity(BakedModelRenderer renderManager, BlockState state, MatrixStack matrices, IntFunction<VertexConsumer> vertexConsumers, int light, int overlay, BlockView blockView, BlockPos pos) {

    }

    private final Supplier<BakedModelRenderer> bakedModelRenderer = Suppliers.memoize(BakedModelRendererImpl::new);
    private final Supplier<StateManager> stateManager = Suppliers.memoize(StateManagerImpl::new);
}
