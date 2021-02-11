package net.modificationstation.stationapi.api.common.event.mod;

import net.modificationstation.stationapi.api.common.event.ModEventOld;
import net.modificationstation.stationapi.api.common.registry.ModID;

/**
 * PostInitialization event called for mods to do their setup when the other mods finished initializing.
 * Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
public interface PostInit {

    /**
     * The event instance.
     */
    ModEventOld<PostInit> EVENT = new ModEventOld<>(PostInit.class,
            listeners ->
                    modID -> {
                        for (PostInit listener : listeners)
                            listener.postInit(PostInit.EVENT.getListenerModID(listener));
                    },
            listener ->
                    modID -> {
                        PostInit.EVENT.setCurrentListener(listener);
                        listener.postInit(modID);
                        PostInit.EVENT.setCurrentListener(null);
                    },
            postInit ->
                    postInit.register(modID -> ModEventOld.post(new Data()), null)
    );

    /**
     * The event function.
     * @param modID current listener's ModID.
     */
    void postInit(ModID modID);

    /**
     * The event data used by EventBus.
     */
    final class Data extends ModInitData<PostInit> {

        private Data() {
            super(EVENT);
        }
    }
}
