package net.modificationstation.sltest.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.sltest.celestial.CelestialListener;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class CelestialTestItem extends TemplateItem {
    public CelestialTestItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ItemStack use(ItemStack item, World world, PlayerEntity player) {
        if (CelestialListener.flyingDimando.isActive()) System.out.println("Event is happening");
        else System.out.println("No event");
        return super.use(item, world, player);
    }
}
