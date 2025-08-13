package net.modificationstation.stationapi.api.client.render.model.fluid;

import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.InputContext;

/**
 * Identical in operation to {@link BakedModel} but for fluids.
 *
 * <p>A compliant renderer will call this - in addition to the block quad emitter - for
 * block state with a non-empty fluid state.  Block state is passed instead of fluid state
 * to keep the method signature compact and provide access to the block state if needed.
 */
@FunctionalInterface
public interface FluidModel {
    void emitQuads(InputContext context, QuadEmitter output);
}
