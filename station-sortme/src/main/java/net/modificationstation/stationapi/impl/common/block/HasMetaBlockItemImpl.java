package net.modificationstation.stationapi.impl.common.block;

import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.block.HasMetaBlockItem;
import net.modificationstation.stationapi.api.block.HasMetaNamedBlockItem;
import net.modificationstation.stationapi.api.block.IHasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.block.IHasMetaBlockItem;
import net.modificationstation.stationapi.api.block.IHasMetaNamedBlockItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.block.BlockEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.template.common.item.MetaBlock;

import java.util.function.*;

/**
 * {@link IHasMetaBlockItem} implementation class.
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
public class HasMetaBlockItemImpl {

    /**
     * Handles block's {@link HasMetaBlockItem} annotation if it's present via {@link BlockEvent.ItemFactory} hook.
     * @param event blockitemfactory callback.
     */
    @EventListener(priority = ListenerPriority.HIGH)
    private static void getBlockItemFactory(BlockEvent.ItemFactory event) {
        if (event.block.getClass().isAnnotationPresent(HasMetaBlockItem.class))
            event.currentFactory = FACTORY;
    }

    /**
     * {@link MetaBlock#MetaBlock(int)} field.
     */
    public static final IntFunction<Block> FACTORY = MetaBlock::new;
}
