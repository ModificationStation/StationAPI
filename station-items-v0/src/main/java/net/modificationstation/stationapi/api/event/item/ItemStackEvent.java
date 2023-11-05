package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@SuperBuilder
public abstract class ItemStackEvent extends Event {
    public final ItemStack itemStack;

    @SuperBuilder
    public static class Crafted extends ItemStackEvent {
        public final World world;
        public final PlayerEntity player;
    }
}
