package net.modificationstation.stationapi.impl.transform;

import net.mine_diver.unsafeevents.transform.EventSubclassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.transformers.TreeTransformer;

class MixinTransformerHook<T extends TreeTransformer & IMixinTransformer> extends MixinTransformerDelegate<T> {
    MixinTransformerHook(T delegate) {
        super(delegate);
    }

    @Override
    public byte[] transformClassBytes(String name, String transformedName, byte[] basicClass) {
        if (
                basicClass != null &&
                        !name.startsWith("org.objectweb.asm.") &&
                        !name.equals("net.mine_diver.unsafeevents.transform.EventSubclassTransformer") &&
                        EventSubclassTransformer.handles(name)
        ) {
            ClassNode classNode = new ClassNode();
            new ClassReader(basicClass).accept(classNode, 0);
            if (EventSubclassTransformer.transform(Thread.currentThread().getContextClassLoader(), classNode)) {
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classNode.accept(classWriter);
                basicClass = classWriter.toByteArray();
            }
        }
        return super.transformClassBytes(name, transformedName, basicClass);
    }
}
