package net.modificationstation.stationapi.api.event.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.function.BooleanSupplier;

@SuperBuilder
public abstract class BlockEvent extends Event {
    public final Block block;

    @SuperBuilder
    public static final class TranslationKeyChanged extends BlockEvent {
        public String currentTranslationKey;
    }

    @Cancelable
    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static final class BeforeRemoved extends BlockEvent {
        public final World level;
        public final int x, y, z;
    }

    @Cancelable
    @SuperBuilder
    public static final class BeforeDrop extends BlockEvent {
        public final World level;
        public final int x, y, z, meta;
        public final float chance;
    }

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static final class BeforePlacedByItem extends BlockEvent {
        public final World world;
        public final PlayerEntity player;
        public final int x, y, z, meta;
        public final Direction side;
        public final ItemStack blockItem;
        public BooleanSupplier placeFunction;
    }
}
