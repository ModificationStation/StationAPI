package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.material;

import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;

public class RenderMaterialImpl extends MaterialViewImpl implements RenderMaterial {
    public static final int VALUE_COUNT = 1 << TOTAL_BIT_LENGTH;
    private static final RenderMaterialImpl[] BY_INDEX = new RenderMaterialImpl[VALUE_COUNT];

    static {
        for (int i = 0; i < VALUE_COUNT; i++) {
            if (areBitsValid(i)) {
                BY_INDEX[i] = new RenderMaterialImpl(i);
            }
        }
    }

    private RenderMaterialImpl(int bits) {
        super(bits);
    }

    public int index() {
        return bits;
    }

    @Override
    public String toString() {
        return "RenderMaterialImpl{" + contentsToString() + "}";
    }

    public static RenderMaterialImpl byIndex(int index) {
        return BY_INDEX[index];
    }

    public static RenderMaterialImpl setDisableDiffuse(RenderMaterialImpl material, boolean disable) {
        if (material.disableDiffuse() != disable) {
            return byIndex(disable ? (material.bits | DIFFUSE_FLAG) : (material.bits & ~DIFFUSE_FLAG));
        }

        return material;
    }
}
