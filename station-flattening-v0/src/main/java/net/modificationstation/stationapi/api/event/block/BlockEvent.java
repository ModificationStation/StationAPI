package net.modificationstation.stationapi.api.event.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.function.BooleanSupplier;

@SuperBuilder
public abstract class BlockEvent extends Event {
    public final BlockBase block;

    @SuperBuilder
    public static final class TranslationKeyChanged extends BlockEvent {
        public String currentTranslationKey;
    }

    @Cancelable
    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static final class BeforeRemoved extends BlockEvent {
        public final Level level;
        public final int x, y, z;
    }

    @Cancelable
    @SuperBuilder
    public static final class BeforeDrop extends BlockEvent {
        public final Level level;
        public final int x, y, z, meta;
        public final float chance;
    }

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static final class BeforePlacedByItem extends BlockEvent {
        public final Level world;
        public final PlayerBase player;
        public final int x, y, z, meta;
        public final Direction side;
        public final ItemInstance blockItem;
        public BooleanSupplier placeFunction;
    }
}
