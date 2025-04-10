package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.material;

import net.modificationstation.stationapi.api.client.render.material.MaterialView;
import net.modificationstation.stationapi.api.client.render.material.ShadeMode;
import net.modificationstation.stationapi.api.util.TriState;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.Locale;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.bitMask;

/**
 * Default implementation of the standard render materials.
 * The underlying representation is simply an int with bit-wise
 * packing of the various material properties. This offers
 * easy/fast interning via int/object hashmap.
 */
public class MaterialViewImpl implements MaterialView {
    private static final TriState[] TRI_STATES = TriState.values();
    private static final int TRI_STATE_COUNT = TRI_STATES.length;
    private static final ShadeMode[] SHADE_MODES = ShadeMode.values();
    private static final int SHADE_MODE_COUNT = SHADE_MODES.length;

    protected static final int EMISSIVE_BIT_LENGTH = 1;
    protected static final int DIFFUSE_BIT_LENGTH = 1;
    protected static final int AO_BIT_LENGTH = MathHelper.ceilLog2(TRI_STATE_COUNT);
    protected static final int SHADE_MODE_BIT_LENGTH = MathHelper.ceilLog2(SHADE_MODE_COUNT);

    protected static final int EMISSIVE_BIT_OFFSET = 0;
    protected static final int DIFFUSE_BIT_OFFSET = EMISSIVE_BIT_OFFSET + EMISSIVE_BIT_LENGTH;
    protected static final int AO_BIT_OFFSET = DIFFUSE_BIT_OFFSET + DIFFUSE_BIT_LENGTH;
    protected static final int SHADE_MODE_BIT_OFFSET = AO_BIT_OFFSET + AO_BIT_LENGTH;
    public static final int TOTAL_BIT_LENGTH = SHADE_MODE_BIT_OFFSET + SHADE_MODE_BIT_LENGTH;

    protected static final int EMISSIVE_FLAG = bitMask(EMISSIVE_BIT_LENGTH, EMISSIVE_BIT_OFFSET);
    protected static final int DIFFUSE_FLAG = bitMask(DIFFUSE_BIT_LENGTH, DIFFUSE_BIT_OFFSET);
    protected static final int AO_MASK = bitMask(AO_BIT_LENGTH, AO_BIT_OFFSET);
    protected static final int SHADE_MODE_MASK = bitMask(SHADE_MODE_BIT_LENGTH, SHADE_MODE_BIT_OFFSET);

    protected static boolean areBitsValid(int bits) {
        int ao = (bits & AO_MASK) >>> AO_BIT_OFFSET;
        int shadeMode = (bits & SHADE_MODE_MASK) >>> SHADE_MODE_BIT_OFFSET;

        return ao < TRI_STATE_COUNT
                && shadeMode < SHADE_MODE_COUNT;
    }

    protected int bits;

    protected MaterialViewImpl(int bits) {
        this.bits = bits;
    }

    @Override
    public boolean emissive() {
        return (bits & EMISSIVE_FLAG) != 0;
    }

    @Override
    public boolean disableDiffuse() {
        return (bits & DIFFUSE_FLAG) != 0;
    }

    @Override
    public TriState ambientOcclusion() {
        return TRI_STATES[(bits & AO_MASK) >>> AO_BIT_OFFSET];
    }

    @Override
    public ShadeMode shadeMode() {
        return SHADE_MODES[(bits & SHADE_MODE_MASK) >>> SHADE_MODE_BIT_OFFSET];
    }

    /**
     * Returns a string representation of the material properties.
     * To be used in {@link #toString} overrides so they show in the debugger.
     */
    String contentsToString() {
        return String.format("emissive=%b, disable diffuse=%b, ao=%s, shade=%s",
                emissive(),
                disableDiffuse(),
                ambientOcclusion().toString().toLowerCase(Locale.ROOT),
                shadeMode().toString().toLowerCase(Locale.ROOT));
    }
}
