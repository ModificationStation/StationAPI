package net.modificationstation.stationapi.api.common.event;

import net.modificationstation.stationapi.impl.common.util.UnsafeProvider;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_8;

final class ListenerRegistryFactory {

    private final Class<? extends EventBus> eventBus;

    ListenerRegistryFactory(EventBus eventBus) {
        this.eventBus = eventBus.getClass();
    }

    private Class<? extends Consumer<? extends Event>> generateExecutor(ListenerContainer[] listenerContainers) {
        //noinspection unchecked
        return (Class<? extends Consumer<? extends Event>>)
                UnsafeProvider.getUnsafe().defineAnonymousClass(
                        eventBus,
                        generateExecutorClass(
                                eventBus.getName().replace('.', '/') + "$$ListenerRegistry",
                                listenerContainers
                        ),
                        null
                ).asSubclass(Consumer.class);
    }

    private static byte[] generateExecutorClass(String name, ListenerContainer[] listenerContainers) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        writer.visit(V1_8, ACC_PUBLIC, name, null, "java/lang/Object", new String[] { Type.getInternalName(Consumer.class) });
        // Generate fields
        for (int i = 0; i < listenerContainers.length; i++)
            writer.visitField(ACC_PRIVATE, "instance" + i, Type.getType(Consumer.class).getDescriptor(), null, null).visitEnd();
        // Generate constructor
        StringBuilder corDesc = new StringBuilder();
        for (int i = 0; i < listenerContainers.length; i++)
            corDesc.append(Type.getType(ListenerContainer.class).getDescriptor());
        MethodVisitor methodGenerator = writer.visitMethod(ACC_PUBLIC, "<init>", "(" + corDesc + ")V", null, null);
        methodGenerator.visitCode();
        methodGenerator.visitVarInsn(ALOAD, 0);
        methodGenerator.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        for (int i = 0; i < listenerContainers.length; i++) {
            methodGenerator.visitVarInsn(ALOAD, 0);
            methodGenerator.visitVarInsn(ALOAD, i + 1);
            methodGenerator.visitFieldInsn(GETFIELD, Type.getInternalName(ListenerContainer.class), "invoker", Type.getType(Consumer.class).getDescriptor());
            methodGenerator.visitFieldInsn(PUTFIELD, name, "instance" + i, Type.getType(Consumer.class).getDescriptor());
        }
        methodGenerator.visitInsn(RETURN);
        methodGenerator.visitMaxs(-1, -1);
        methodGenerator.visitEnd();
        // Generate the execute method
        methodGenerator = writer.visitMethod(ACC_PUBLIC, "accept", "(Ljava/lang/Object;)V", null, null);
        methodGenerator.visitCode();
        for (int i = 0; i < listenerContainers.length; i++) {
            methodGenerator.visitVarInsn(ALOAD, 0);
            methodGenerator.visitFieldInsn(GETFIELD, name, "instance" + i, Type.getType(Consumer.class).getDescriptor());
            methodGenerator.visitVarInsn(ALOAD, 1);
            methodGenerator.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(Consumer.class), "accept", "(Ljava/lang/Object;)V", true);
        }
        methodGenerator.visitInsn(RETURN);
        methodGenerator.visitMaxs(-1, -1);
        methodGenerator.visitEnd();
        writer.visitEnd();
        return writer.toByteArray();
    }

    <T extends Event> Consumer<T> create(ListenerContainer[] listenerContainers) {
        //noinspection unchecked
        Class<? extends Consumer<T>> executorClass = (Class<? extends Consumer<T>>) generateExecutor(listenerContainers);
        try {
            return executorClass.getConstructor(Arrays.stream(listenerContainers).map(ListenerContainer::getClass).toArray(Class[]::new)).newInstance((Object[]) listenerContainers);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Unable to initialize " + executorClass, e);
        }
    }
}
