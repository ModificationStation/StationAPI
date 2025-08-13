package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.material;

import net.modificationstation.stationapi.api.client.render.material.MaterialFinder;
import net.modificationstation.stationapi.api.client.render.material.MaterialView;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.client.render.material.ShadeMode;
import net.modificationstation.stationapi.api.util.TriState;

import java.util.Objects;

public class MaterialFinderImpl extends MaterialViewImpl implements MaterialFinder {
    private static final int DEFAULT_BITS;

    static {
        // Start with all zeroes
        MaterialFinderImpl finder = new MaterialFinderImpl(0);
        // Apply non-zero defaults
        finder.ambientOcclusion(TriState.UNSET);
        DEFAULT_BITS = finder.bits;

        if (!areBitsValid(DEFAULT_BITS)) {
            throw new AssertionError("Default MaterialFinder bits are not valid!");
        }
    }

    protected MaterialFinderImpl(int bits) {
        super(bits);
    }

    public MaterialFinderImpl() {
        this(DEFAULT_BITS);
    }

    @Override
    public MaterialFinder emissive(boolean isEmissive) {
        bits = isEmissive ? (bits | EMISSIVE_FLAG) : (bits & ~EMISSIVE_FLAG);
        return this;
    }

    @Override
    public MaterialFinder disableDiffuse(boolean disable) {
        bits = disable ? (bits | DIFFUSE_FLAG) : (bits & ~DIFFUSE_FLAG);
        return this;
    }

    @Override
    public MaterialFinder ambientOcclusion(TriState mode) {
        Objects.requireNonNull(mode, "ambient occlusion TriState may not be null");

        bits = (bits & ~AO_MASK) | (mode.ordinal() << AO_BIT_OFFSET);
        return this;
    }

    @Override
    public MaterialFinder shadeMode(ShadeMode mode) {
        Objects.requireNonNull(mode, "ShadeMode may not be null");

        bits = (bits & ~SHADE_MODE_MASK) | (mode.ordinal() << SHADE_MODE_BIT_OFFSET);
        return this;
    }

    @Override
    public MaterialFinder copyFrom(MaterialView material) {
        bits = ((MaterialViewImpl) material).bits;
        return this;
    }

    @Override
    public MaterialFinder clear() {
        bits = DEFAULT_BITS;
        return this;
    }

    @Override
    public RenderMaterial find() {
        return RenderMaterialImpl.byIndex(bits);
    }

    @Override
    public String toString() {
        return "MaterialFinderImpl{" + contentsToString() + "}";
    }
}
