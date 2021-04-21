package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.block.CustomBlockItemFactoryProvider;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.block.HasMetaBlockItem;
import net.modificationstation.stationapi.api.block.HasMetaNamedBlockItem;
import net.modificationstation.stationapi.api.block.MetaBlockItemProvider;
import net.modificationstation.stationapi.api.block.MetaNamedBlockItemProvider;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.template.item.MetaNamedBlock;

import java.util.function.*;

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
public class HasMetaNamedBlockItemImpl {

    /**
     * Handles block's {@link HasMetaNamedBlockItem} annotation if it's present via {@link BlockItemFactoryEvent} hook.
     * @param event blockitemfactory callback.
     */
    @EventListener(priority = ListenerPriority.HIGH)
    private static void getBlockItemFactory(BlockItemFactoryEvent event) {
        if (event.block.getClass().isAnnotationPresent(HasMetaNamedBlockItem.class))
            event.currentFactory = FACTORY;
    }

    /**
     * {@link MetaNamedBlock#MetaNamedBlock(int)} field.
     */
    @SuppressWarnings("Convert2MethodRef") // Method references load their target classes, which may load ItemBase before it should be loaded normally.
    public static final IntFunction<Block> FACTORY = i -> new MetaNamedBlock(i);
}
