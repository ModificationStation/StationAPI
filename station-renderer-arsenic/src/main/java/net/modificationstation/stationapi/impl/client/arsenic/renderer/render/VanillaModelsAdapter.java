package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.render.model.LoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class VanillaModelsAdapter {

    // EXPERIMENTAL BACKWARDS COMPATIBILITY WITH VANILLA RENDERER.
    // BROKEN, CAN ONLY BAKE FULL-BLOCK MODELS, DOES NOT SUPPORT COLOUR PROVIDERS AND CAN NOT ADJUST RENDERER SIZES AND TEXTURES ON THE FLY (PORTAL, GRASS BLOCK)
    // IMPLEMENTING THE LATTER MAY INDUCE LARGE PERFORMANCE DROPS
    @EventListener
    private static void registerModel(LoadUnbakedModelEvent event) {
//        Identifier id = event.getIdentifier();
//        ModelIdentifier modelId = ModelIdentifier.of(id.toString());
//        if (modelId.id.modID == ModID.MINECRAFT) {
//            Identifier itemId = modelId.id.id.startsWith("item/") ? Identifier.of(modelId.id.modID, modelId.id.id.substring(5)) : modelId.id;
//            ItemRegistry.INSTANCE.get(itemId).ifPresent(itemBase -> event.model = new VanillaUnbakedModel(itemId));
//        }
    }
}
