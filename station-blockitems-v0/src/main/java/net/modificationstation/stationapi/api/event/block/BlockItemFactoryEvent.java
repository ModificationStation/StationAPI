package net.modificationstation.stationapi.api.event.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.item.BlockItem;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

/**
 * When blocks are being registered, this event is called to make it possible to replace the default block item for the current block.
 * @author mine_diver
 * @see CustomBlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see MetaBlockItemProvider
 * @see HasMetaBlockItem
 * @see MetaNamedBlockItemProvider
 * @see HasMetaNamedBlockItem
 */
@Cancelable
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class BlockItemFactoryEvent extends BlockEvent {
    /**
     * Current factory that's going to be executed to get block item instance.
     * <p>Can not return null due to limitations from mixin's side. Cancel instead.
     */
    public @NotNull IntFunction<@NotNull BlockItem> currentFactory;
}
