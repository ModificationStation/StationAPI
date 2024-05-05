package net.modificationstation.stationapi.impl.config;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationapi.api.config.PostConfigLoadedListener;
import net.modificationstation.stationapi.api.config.PreConfigSavedListener;

import java.util.*;

public class EventStorage {
    public static final Map<String, EntrypointContainer<PreConfigSavedListener>> PRE_SAVE_LISTENERS = new HashMap<>();
    public static final Map<String, EntrypointContainer<PostConfigLoadedListener>> POST_LOAD_LISTENERS = new HashMap<>();

    public static void loadListeners() {
        FabricLoader.getInstance().getEntrypointContainers("gcapi:presave", PreConfigSavedListener.class).forEach(preConfigSavedListenerEntrypointContainer -> PRE_SAVE_LISTENERS.put(preConfigSavedListenerEntrypointContainer.getProvider().getMetadata().getId(), preConfigSavedListenerEntrypointContainer));
        FabricLoader.getInstance().getEntrypointContainers("gcapi:postload", PostConfigLoadedListener.class).forEach(postConfigLoadedListenerEntrypointContainer -> POST_LOAD_LISTENERS.put(postConfigLoadedListenerEntrypointContainer.getProvider().getMetadata().getId(), postConfigLoadedListenerEntrypointContainer));
    }

    /**
     * Used in the save and load listeners. Use bitwise operations, or the helper methods to tell them from the source int.
     */
    public static class EventSource {
        public static final int UNKNOWN = 0;
        public static final int GAME_LOAD = 1<<1;
        public static final int MODDED_SERVER_JOIN = 1<<2;
        public static final int VANILLA_SERVER_JOIN = 1<<3;
        public static final int SERVER_JOIN = 1<<4;
        public static final int USER_SAVE = 1<<5;
        public static final int MOD_SAVE = 1<<6;
        public static final int SERVER_EXPORT = 1<<7;

        /**
         * Helper method for those unfamiliar with bitwise stuff.
         * If ALL needles are inside the haystack, return true.
         */
        public static boolean containsAll(int haystack, int... needles) {
            int needlesBitwise = 0;
            for(int value : needles) {
                needlesBitwise |= value;
            }
            return (haystack & needlesBitwise) != 0;
        }

        /**
         * Helper method for those unfamiliar with bitwise stuff.
         * If ONE of needles is inside the haystack, return true.
         */
        public static boolean containsOne(int haystack, int... needles) {
            for(int value : needles) {
                if((haystack & value) != 0) {
                    return true;
                }
            }
            return false;
        }
    }
}
