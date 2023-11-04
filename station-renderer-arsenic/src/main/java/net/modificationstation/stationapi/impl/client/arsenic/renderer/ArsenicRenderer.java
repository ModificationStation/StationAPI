package net.modificationstation.stationapi.impl.client.arsenic.renderer;

import com.google.common.base.Suppliers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.material.MaterialFinder;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.client.render.mesh.MeshBuilder;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MeshBuilderImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.BakedModelRendererImpl;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArsenicRenderer implements Renderer {

    public static final ArsenicRenderer INSTANCE = new ArsenicRenderer();

    public static final RenderMaterialImpl.Value MATERIAL_STANDARD = (RenderMaterialImpl.Value) INSTANCE.materialFinder().find();

    static {
        INSTANCE.registerMaterial(RenderMaterial.MATERIAL_STANDARD, MATERIAL_STANDARD);
    }

    private final Map<Identifier, RenderMaterial> materialMap = new IdentityHashMap<>();

    @Override
    public MeshBuilder meshBuilder() {
        return new MeshBuilderImpl();
    }

    @Override
    public MaterialFinder materialFinder() {
        return new RenderMaterialImpl.Finder();
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
    public BakedModelRenderer bakedModelRenderer() {
        return bakedModelRenderer.get();
    }

    private final Supplier<BakedModelRenderer> bakedModelRenderer = Suppliers.memoize(BakedModelRendererImpl::new);
}
