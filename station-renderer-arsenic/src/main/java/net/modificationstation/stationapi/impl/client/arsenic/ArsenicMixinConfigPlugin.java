package net.modificationstation.stationapi.impl.client.arsenic;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.*;

public class ArsenicMixinConfigPlugin implements IMixinConfigPlugin {

    /**
     *  Set by other renderers to disable loading of Arsenic.
     */
    private static final String JSON_KEY_DISABLE_ARSENIC = "station-renderer-api-v0:contains_renderer";
    /**
     * Disables vanilla block tesselation and ensures vertex format compatibility.
     */
    private static final String JSON_KEY_FORCE_COMPATIBILITY = "station-renderer-arsenic:force_compatibility";

    private static boolean needsLoad = true;

    private static boolean arsenicApplicable = true;
    private static boolean forceCompatibility = false;

    private static void loadIfNeeded() {
        if (needsLoad) {
            for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
                final ModMetadata meta = container.getMetadata();

                if (meta.containsCustomValue(JSON_KEY_DISABLE_ARSENIC)) {
                    arsenicApplicable = false;
                } else if (meta.containsCustomValue(JSON_KEY_FORCE_COMPATIBILITY)) {
                    forceCompatibility = true;
                }
            }

            needsLoad = false;
        }
    }

    static boolean shouldApplyArsenic() {
        loadIfNeeded();
        return arsenicApplicable;
    }

    static boolean shouldForceCompatibility() {
        loadIfNeeded();
        return forceCompatibility;
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return shouldApplyArsenic();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
