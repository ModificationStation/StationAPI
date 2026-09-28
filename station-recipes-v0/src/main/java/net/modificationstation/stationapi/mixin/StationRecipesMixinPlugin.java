package net.modificationstation.stationapi.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class StationRecipesMixinPlugin implements IMixinConfigPlugin {
    private boolean shouldLoad = false;
    
    public boolean shouldLoad() {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return false;
        }

        if (!FabricLoader.getInstance().isModLoaded("alwaysmoreitems")) {
            return false;
        }

        boolean enableAmiCompat = false;
        String[] launchArgs = FabricLoader.getInstance().getLaunchArguments(true);
        for (String arg : launchArgs) {
            if (arg.contains("enableAmiCompat")) {
                enableAmiCompat = true;
                break;
            }
        }

        return enableAmiCompat;
    }

    @Override
    public void onLoad(String mixinPackage) {
        this.shouldLoad = shouldLoad();
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
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (shouldLoad && "net.modificationstation.stationapi.impl.recipe.StationShapedRecipe".equals(targetClassName)) {
            boolean exists = targetClass.methods.stream().anyMatch(m -> m.name.equals("method_2073"));

            if (!exists) {
                MethodNode bridge = new MethodNode(
                        Opcodes.ACC_PUBLIC,
                        "method_2073",
                        "()Lnet/minecraft/item/ItemStack;",
                        null,
                        null
                );

                // return this.getOutput();
                bridge.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                bridge.instructions.add(new MethodInsnNode(
                        Opcodes.INVOKEVIRTUAL,
                        targetClass.name,
                        "getOutput",
                        "()Lnet/minecraft/item/ItemStack;",
                        false
                ));
                bridge.instructions.add(new InsnNode(Opcodes.ARETURN));

                targetClass.methods.add(bridge);
            }
        }

        if (shouldLoad && "net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe".equals(targetClassName)) {
            boolean exists = targetClass.methods.stream().anyMatch(m -> m.name.equals("method_2073"));

            if (!exists) {
                MethodNode bridge = new MethodNode(
                        Opcodes.ACC_PUBLIC,
                        "method_2073",
                        "()Lnet/minecraft/item/ItemStack;",
                        null,
                        null
                );

                // return this.getOutput();
                bridge.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                bridge.instructions.add(new MethodInsnNode(
                        Opcodes.INVOKEVIRTUAL,
                        targetClass.name,
                        "getOutput",
                        "()Lnet/minecraft/item/ItemStack;",
                        false
                ));
                bridge.instructions.add(new InsnNode(Opcodes.ARETURN));

                targetClass.methods.add(bridge);
            }
        }
    }
}
