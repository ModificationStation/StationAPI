package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.BlockItem;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.*;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.template.item.MetaNamedBlockItem;

import java.util.function.IntFunction;

/**
 * {@link MetaNamedBlockItemProvider} implementation class.
 * @author mine_diver
 * @see BlockItemFactoryEvent
 * @see CustomBlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see MetaBlockItemProvider
 * @see HasMetaBlockItem
 * @see MetaNamedBlockItemProvider
 * @see HasMetaNamedBlockItem
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class HasMetaNamedBlockItemImpl {
    /**
     * Handles block's {@link HasMetaNamedBlockItem} annotation if it's present via {@link BlockItemFactoryEvent} hook.
     * @param event blockitemfactory callback.
     */
    @EventListener
    private static void getBlockItemFactory(BlockItemFactoryEvent event) {
        if (event.block.getClass().isAnnotationPresent(HasMetaNamedBlockItem.class))
            event.currentFactory = FACTORY;
    }

    /**
     * {@link MetaNamedBlockItem#MetaNamedBlockItem(int)} field.
     */
    @SuppressWarnings("Convert2MethodRef") // Method references load their target classes, which may load Item before it should be loaded normally.
    public static final IntFunction<BlockItem> FACTORY = i -> new MetaNamedBlockItem(i);
}
