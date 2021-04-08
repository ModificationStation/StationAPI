package net.modificationstation.stationapi.impl.common.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.common.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.common.block.HasMetaBlockItem;
import net.modificationstation.stationapi.api.common.block.HasMetaNamedBlockItem;
import net.modificationstation.stationapi.api.common.block.IHasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.common.block.IHasMetaBlockItem;
import net.modificationstation.stationapi.api.common.block.IHasMetaNamedBlockItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.block.BlockEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.lang.invoke.*;
import java.util.function.*;

/**
 * {@link IHasCustomBlockItemFactory} implementation class.
 * @author mine_diver
 * @see BlockEvent.ItemFactory
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class HasCustomBlockItemFactoryImpl {

    /**
     * Processes {@link HasCustomBlockItemFactory} annotation if present via {@link BlockEvent.ItemFactory} hook.
     * @param event blockitemfactory callback.
     */
    @EventListener(priority = ListenerPriority.HIGH)
    private static void getBlockItemFactory(BlockEvent.ItemFactory event) {
        if (event.block instanceof IHasCustomBlockItemFactory)
            event.currentFactory = ((IHasCustomBlockItemFactory) event.block).getBlockItemFactory();
        Class<? extends BlockBase> blockClass = event.block.getClass();
        if (blockClass.isAnnotationPresent(HasCustomBlockItemFactory.class)) {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            try {
                //noinspection unchecked
                event.currentFactory = (IntFunction<Block>) LambdaMetafactory.metafactory(
                        lookup,
                        "apply",
                        MethodType.methodType(IntFunction.class),
                        MethodType.methodType(Object.class, int.class),
                        lookup.findConstructor(blockClass.getAnnotation(HasCustomBlockItemFactory.class).value(), MethodType.methodType(void.class, int.class)),
                        MethodType.methodType(Block.class, int.class)
                ).getTarget().invokeExact();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
