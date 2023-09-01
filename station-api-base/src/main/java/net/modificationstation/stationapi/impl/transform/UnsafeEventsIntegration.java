package net.modificationstation.stationapi.impl.transform;

import net.mine_diver.spasm.api.transform.ClassTransformer;
import net.mine_diver.spasm.api.transform.TransformationResult;
import net.mine_diver.unsafeevents.transform.EventSubclassTransformer;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;

public final class UnsafeEventsIntegration implements ClassTransformer {
    @Override
    public @NotNull TransformationResult transform(@NotNull ClassLoader classLoader, @NotNull ClassNode classNode) {
        return !"net/mine_diver/unsafeevents/transform/EventSubclassTransformer".equals(classNode.name) &&
                EventSubclassTransformer.handles(classNode.name.replace('/', '.')) &&
                EventSubclassTransformer.transform(classLoader, classNode) ?
                TransformationResult.SUCCESS : TransformationResult.PASS;
    }
}
