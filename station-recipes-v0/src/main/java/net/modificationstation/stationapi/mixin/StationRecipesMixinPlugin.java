package net.modificationstation.stationapi.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.StationAPI;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StationRecipesMixinPlugin implements IMixinConfigPlugin {
    private static final ArrayList<String> AMI_MIXINS = new ArrayList<>();

    static {
        AMI_MIXINS.add("dev.StationShapedRecipeMixin");
        AMI_MIXINS.add("dev.StationShapelessRecipeMixin");
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return null;
        }

        if (!FabricLoader.getInstance().isModLoaded("alwaysmoreitems")) {
            return null;
        }

        boolean enableAmiCompat = false;
        String[] launchArgs = FabricLoader.getInstance().getLaunchArguments(true);
        for (String arg : launchArgs) {
            if (arg.contains("enableAmiCompat")) {
                enableAmiCompat = true;
                break;
            }
        }

        if (!enableAmiCompat) {
            return null;
        }

        StationAPI.LOGGER.info("AlwaysMoreItems enabled in development environment, adding mixins to make it work");

        return AMI_MIXINS;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
