package net.modificationstation.stationapi.impl.transform;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.transformers.TreeTransformer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class MixinConfigPluginHook implements IMixinConfigPlugin {
    static {
        try {
            hook();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends TreeTransformer & IMixinTransformer> void hook() throws NoSuchFieldException, IllegalAccessException {
        ClassLoader knotClassLoader = MixinConfigPluginHook.class.getClassLoader();
        Field knotClassDelegateField = knotClassLoader.getClass().getDeclaredField("delegate");
        knotClassDelegateField.setAccessible(true);
        Object knotClassDelegate = knotClassDelegateField.get(knotClassLoader);
        Field mixinTransformerField = knotClassDelegate.getClass().getDeclaredField("mixinTransformer");
        mixinTransformerField.setAccessible(true);
        //noinspection unchecked
        mixinTransformerField.set(knotClassDelegate, new MixinTransformerHook<>((T) mixinTransformerField.get(knotClassDelegate)));
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
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
